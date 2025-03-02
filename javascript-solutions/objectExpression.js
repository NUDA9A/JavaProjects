// :NOTE: не прошли тесты
"use strict";

let variables = {'x': 0, 'y': 1, 'z': 2};

function Expression(f, op) {
    this.f = f;
    this.op = op;
    return function(...args) {
        this.args = args;
    };
}

Expression.prototype.evaluate = function (x, y, z) { return this.f(...(this.args.map(arg => arg.evaluate(x, y, z)))); }
Expression.prototype.toString = function() { return this.args.map(arg => arg.toString()).join(" ") + " " + this.op; }
Expression.prototype.prefix = function() { return "(" + this.op + " " + this.args.map(arg => arg.prefix()).join(" ") + ")"; }

function makeExpression(...args) {
    let operation = function(...args1) {
        Expression.apply(this, args).apply(this, args1);
    }
    operation.prototype = Expression.prototype;
    return operation;
}

const meanEval = (...args) => {
    let suma = 0;
    args.map(arg => suma += arg);
    return suma / args.length;
}

const varEval = (...args) => {
    let mean = meanEval(...args);
    let suma = 0;
    args.map(arg => suma += ((Math.pow(arg - mean, 2))) / (args.length));
    return suma;
}

const Add = makeExpression((a, b) => (a + b), "+");
const Subtract = makeExpression((a, b) => (a - b), "-");
const Multiply = makeExpression((a, b) => (a * b), "*");
const Divide = makeExpression((a, b) => (a / b), "/");
const Negate = makeExpression((a) => -(a), "negate");
const Sin = makeExpression((Math.sin), "sin");
const Cos = makeExpression((Math.cos), "cos");
const Mean = makeExpression((meanEval), "mean");
const Var = makeExpression((varEval), "var");

// :NOTE: prototype

function Const(cnst) {
    this.evaluate = (x, y, z) => cnst;
    this.toString = () => cnst.toString();
    this.prefix = () => cnst.toString();
}

function Variable(v) {
    this.evaluate = (...args) => args[variables[v]];
    this.toString = () => v;
    this.prefix = () => v;
}

let ops = {
    "+": [Add, 2],
    "-": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2],
    "negate": [Negate, 1],
    "sin": [Sin, 1],
    "cos": [Cos, 1],
    "mean": [Mean, 0],
    "var": [Var, 0]
}

const IllegalStatement = Error;
const WrongParenthesesSequence = Error;
const UnknownSymbol = Error;

let meanVarArgs = [];

function parse(s, pref) {
    function isDigit(str) {
        for (let i = 0; i < str.length; i++) {
            if (isNaN(parseInt(str[i])) && str[i] !== '-') {
                return false;
            }
        }
        return true;
    }

    let parts = s.trim().split(/\s+/);
    // :NOTE: use split
    let result = [];
    for (let i = 0; i < parts.length; i++) {
        if (parts[i] in ops) {
            let temp = [];
            if (result.length < ops[parts[i]][1]) {
                throw new IllegalStatement("Not enough operands in the expression.");
            }
            let operandsAmmount = parts[i] === "mean" || parts[i] === "var" ? meanVarArgs.pop() : ops[parts[i]][1];
            for (let j = 0; j < operandsAmmount; j++) {
                temp.push(result.pop());
            }
            if (pref) {
                result.push(new ops[parts[i]][0](...(temp)));
            } else {
                result.push(new ops[parts[i]][0](...(temp.reverse())));
            }
        } else {
            if (parts[i] in variables) {
                result.push(new Variable(parts[i]));
            } else {
                if (!isDigit(parts[i])) {
                    throw new UnknownSymbol("Unknown symbol: " + parts[i]);
                }
                result.push(new Const(parseInt(parts[i])));
            }
        }
    }
    if (result.length !== 1) {
        throw new IllegalStatement("You missed smth (probably operation) in your expression.");
    }
    return result[0];
}

function parsePrefix(s) {
    if (s === "") {
        throw new IllegalStatement("No Expression.");
    }

    meanVarArgs = [];
    let i = 0;
    let cnt = 0;
    let tmp = [];
    s = s.trim();
    function update_new_s(k, m) {
        tmp.push(s.substring(k, m));
        i = m + 1;
    }

    function skipWhitespaces(j) {
        while (j < s.length && s[j] === ' ') {
            j++;
        }
        return j;
    }

    function checkOp(j) {
        let l = j + 1;
        l = skipWhitespaces(l);
        let k = l;
        while (k < s.length && s[k] !== ' ' && s[k] !== '(') {
            k++;
        }
        return [l, k];
    }

    function skipBrackets(j) {
        while (j < s.length && s[j] !== ')') {
            if (s[j] === '(') {
                j = skipBrackets(j + 1);
            } else {
                j += 1;
            }
        }
        return j + 1;
    }

    function getArgsAmmount(j) {
        let count = 0;
        while (j < s.length && s[j] !== ')') {
            if (s[j] === '(') {
                count += 1;
                j = skipBrackets(j + 1);
            } else {
                if (s[j] === ' ') {
                    j = skipWhitespaces(j);
                } else {
                    count += 1;
                    let l = j;
                    while (j < s.length && s[j] !== ' ' && s[j] !== ')' && s[j] !== '(') {
                        j += 1;
                    }
                    if (s.substring(l, j) in ops) {
                        throw new IllegalStatement("Wrong arguments: " + s.substring(l, j) + " at pos: " + l);
                    }
                }
            }
        }

        return count;
    }

    for (let j = 0; j < s.length; j++) {
        if (s[j] === "(") {
            let lk = checkOp(j);
            let op = s.substring(lk[0], lk[1]);
            if (!(op in ops)) {
                throw new IllegalStatement("No operation error at pos: " + (j + 1));
            }
            let argsAmmount = getArgsAmmount(lk[1]);
            if (op === "mean" || op === "var") {
                meanVarArgs.push(argsAmmount);
            }
            update_new_s(i, j);
            cnt++;
        }

        if (s[j] === ")") {
            update_new_s(i, j);
            cnt--;
            if (cnt < 0) {
                throw new WrongParenthesesSequence("No opening bracket for closing bracket at pos: " + j);
            }
        }
    }
    if (cnt !== 0) {
        throw new WrongParenthesesSequence("No closing bracket.");
    }
    tmp.push(s.substring(i,));

    let new_s = tmp.join(" ");
    if (new_s === "") {
        new_s = s;
    }
    new_s = new_s.split(/\s+/).reverse().join(" ");

    return parse(new_s, true);
}
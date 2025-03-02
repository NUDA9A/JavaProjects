"use strict";

// :NOTE: Можно ещё чуть упростить
let evaluate = (func) => (...args) => (...args2) => {
    return func(...(args.map(arg => {
        return arg(...args2)
    })));
}

// :NOTE: const
let variables = {"x": 0, "y": 1, "z": 2};
let cnst = (a) => () => (a);
let variable = (v) => (...args) => (args[variables[v]]);
let add = evaluate((a, b) => (a + b));
let subtract = evaluate((a, b) => (a - b));
let multiply = evaluate((a, b) => (a * b));
let divide = evaluate((a, b) => (a / b));
let negate = evaluate((a) => -(a));
// :NOTE: Math.sqrt
let sqrt = evaluate(Math.sqrt);
let square = evaluate((a) => (a * a));
let pi = () => (Math.PI);
let e = () => (Math.E);

let ex = add(subtract(multiply(variable("x"), variable("x")), multiply(cnst(2), variable("x"))), cnst(1));
for (let i = 0; i <= 10; i++) {
    println(ex(i, 0, 0));
}

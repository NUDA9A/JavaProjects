% :NOTE: copy paste
prime(2).
prime(3).
prime(X) :-
	X mod 2 =\= 0,
	\+ is_prime(X, 3).

is_prime(X, Y) :-
	X mod Y =:= 0.

is_prime(X, Y) :- 
	Y * Y < X,
	Y2 is Y + 2,
	is_prime(X, Y2).

nth_prime(1, 2).

nth_prime(2, 3).

nth_prime(N, P) :-
    N > 2,
    nth_prime_func(N, 3, 5, P).

nth_prime_func(N, Current, P, Result) :-
    (prime(P) ->
        (N =:= Current ->
            Result is P;
            Current1 is Current + 1,
            P1 is P + 2,
            nth_prime_func(N, Current1, P1, Result));
        P1 is P + 2,
        nth_prime_func(N, Current, P1, Result)).



composite(X) :- not(prime(X)).

next_div(2, Y1) :- Y1 is 3, !.

next_div(Y, Y1) :- Y1 is 2 + Y.

prime_divisors(1, []) :- !.

prime_divisors(X, Divisors) :-
    X > 1,
    prime_divisors_func(X, 2, [], Divisors).

prime_divisors_func(1, Y, Divisors, Divisors).

prime_divisors_func(X, Y, My_Divisors, Divisors) :-
    X > 1,
    (X mod Y =:= 0 -> X1 is (X // Y),
    append(My_Divisors, [Y], My_Divisors1),
    prime_divisors_func(X1, Y, My_Divisors1, Divisors);
    next_div(Y, Y1),
    prime_divisors_func(X, Y1, My_Divisors, Divisors)).

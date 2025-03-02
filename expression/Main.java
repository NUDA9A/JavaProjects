package expression;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int var = sc.nextInt();
        sc.close();
        int res = new Add(new Subtract(
                new Multiply(
                        new Variable("x"), new Variable("x")
                ),
                new Multiply(
                        new Const(2), new Variable("x")
                )
        ),
                new Const(1)
        ).evaluate(var);

        System.out.println(res);
    }
}

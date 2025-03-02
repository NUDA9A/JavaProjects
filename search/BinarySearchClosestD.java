package search;

public class BinarySearchClosestD {
    //NOTE: контракты для моды правильные, а для базы нет +
    //нужно более формально писать, заменяя язык ("отсортирован", "миниально для любого") на математические высказывания (-)

    //Pred: args.length > 1
    //Post: abs(args[0] - (R == args[i])) - минимально для любого 1 <= i <= args.length - 1
    public static void main(String[] args) {
        //Pred: Integer.parseInt(args[0]) == Integer.parseInt(args[0]) && args[0] - число
        //Post: x == Integer.parseInt(args[0])
        int x = Integer.parseInt(args[0]);

        //Pred: 0 == 0
        //Post: sum == 0
        int sum = 0;
        //Pred: 1 == 1
        //Post: i == 1
        int i = 1;

        //Pred: args.length - 1 >= 0
        //Post: int[] a == new int[args.length - 1]
        int[] a = new int[args.length - 1];

        //Pred: args[i] - число для 1 <= i < args.length
        //Post: a - заполненный массив чисел размера args.length - 1
        while (i < args.length) {
            //Pred: Integer.parseInt(args[i]) == Integer.parseInt(args[i]) && args[i] - число
            //Post: curr == Integer.parseInt(args[i])
            int curr = Integer.parseInt(args[i]);

            //Pred: curr == curr
            //Post: a[i - 1] == curr
            a[i - 1] = curr;

            //Pred: sum' + curr == sum' + curr
            //Post: sum == sum' + curr
            sum += curr;

            //Pred: i' + 1== i' + 1
            //Post: i == i' + 1
            i++;
        }

        //Pred: args.length - 2 >= 0 && x - число
        //Post: вызвали функцию
        if ((sum + x) % 2 == 0) {
            System.out.println(recoursiveBinSearch(a, 0, args.length - 2, x));
        } else {
            System.out.println(iterativeBinSearch(a, 0, args.length - 2, x));
        }
    }

    //Inv: l <= r
    //Pred: a - не возрастает && 0 <= l <= r <= args.length - 2 && x - число
    //Post: R == a[i] && l <= i <= r && abs(x - a[i]) - минимально для любого i
    public static int iterativeBinSearch(int[] a, int l, int r, int x) {
        //Pred: a - не возрастает
        //Post: R == a[r], a[r] - минимально -> abs(Integer.MIN_VALUE - a[r]) - минимально
        if (x == Integer.MIN_VALUE) {
            return a[r];
        }
        //Pred: a - не возрастает
        //Post: R == a[l], a[l] - максимально -> abs(Integer.MAX_VALUE - a[l]) - минимально
        if (x == Integer.MAX_VALUE) {
            return a[l];
        }

        //Inv/Pred: (r' - l') / 2 == r - l || (r' - l') / 2 == r - l - 1
        //Post: 0 <= l <= a.length
        while (l < r) {
            //Pred: (l + r) / 2 == (l + r) / 2
            //Post: m == (l + r) / 2
            int m = (l + r) / 2;
            //Pred: 0 <= m <= a.length - 1 && a невозрастающий
            //Post: x из a[l:m] || x из a[m:r]
            if (a[m] <= x) {
                //Pred: m == m
                //Post: r == m
                r = m;
                //Post1: x из a[l:m]
            } else {
                //Post2: x из a[m:r]
                //Pred: Inv
                //Post: Inv
                if (l == m) {
                    l = m + 1;
                } else {
                    l = m;
                }
            }
        }
        //Pred: a - непустой && l, x - числа
        //Post: abs(x - (R == a[i])) - минимально для любого i && 0 <= i < a.length
        return checkResult(a, l, x);
    }

    //Inv: l <= r
    //Pred: 0 <= l <= r <= a.length && x - число
    //Post: 0 <= l <= a.length
    public static int recoursiveBinSearch(int[] a, int l, int r, int x) {
        //Pred: a - не возрастает
        //Post: R == a[r], a[r] - минимально -> abs(Integer.MIN_VALUE - a[r]) - минимально
        if (x == Integer.MIN_VALUE) {
            return a[r];
        }
        //Pred: a - не возрастает
        //Post: R == a[l], a[l] - максимально -> abs(Integer.MAX_VALUE - a[l]) - минимально
        if (x == Integer.MAX_VALUE) {
            return a[l];
        }

        //Pred: Inv
        //Post: 0 <= l <= a.length
        if (l >= r) {
            //Pred1: l == r && a - непустой && l, x - числа
            //Post: abs(x - (R == a[i])) - минимально для любого i && 0 <= i < a.length
            return checkResult(a, l, x);
        }
        //Pred: (l + r) / 2 == (l + r) / 2
        //Post: m == (l + r) / 2
        int m = (l + r) / 2;

        //Pred: 0 <= m <= a.length - 1 && a невозрастающий
        //Post: x из a[l:m] || x из a[m:r]
        if (a[m] <= x) {
            //Pred1: x из a[l:m]
            return recoursiveBinSearch(a, l, m, x);
            //Post1: l <= R <= m
        } else {
            //Pred2: x из a[m:r]
            //Post2: m <= R <= r
            if (l == m) {
                return recoursiveBinSearch(a, m + 1, r, x);
            } else {
                return recoursiveBinSearch(a, m, r, x);
            }
        }
    }

    //Pred: a - непустой && l,x - числа
    //Post: abs(x - (R == a[i])) - минимально для любого 0 <= i < a.length
    public static int checkResult(int[] a, int l, int x) {
        //Pred: 0 <= l < a.length
        //Post: abs(x - (R == a[i]) - минимально для l - 1 <= i <= l + 1
        if (l - 1 >= 0 && l + 1 < a.length) {
            //Pred1: 0 <= l - 1 < l < l + 1 < a.length
            //Post1: abs(x - (R == a[i]) - минимально для l - 1 <= i <= l + 1
            if (Math.abs(a[l + 1] - x) < Math.abs(a[l - 1] - x) && Math.abs(a[l + 1] - x) < Math.abs(a[l] - x)) {
                //abs(x - a[l + 1]) - минимально для любого l - 1 <= i <= l + 1
                return a[l + 1];
            }
            if (Math.abs(a[l - 1] - x) <= Math.abs(a[l] - x)) {
                //abs(x - a[l - 1]) - минимально для любого l - 1 <= i <= l + 1
                return a[l - 1];
            }
        }
        else {
            //Pred2: 0 <= l - 1 < l < l + 1 == a.length || -1 == l - 1 < l < l + 1 < a.length
            //Post2: abs(x - (R == a[i]) - минимально для l - 1 <= i <= l + 1
            if (l + 1 < a.length && Math.abs(a[l + 1] - x) < Math.abs(a[l] - x)) {
                //abs(x - a[l + 1]) - минимально для любого l - 1 <= i <= l + 1
                return a[l + 1];
            }
            if (l - 1 >= 0 && Math.abs(a[l - 1] - x) <= Math.abs(a[l] - x)) {
                //abs(x - a[l - 1]) - минимально для любого l - 1 <= i <= l + 1
                return a[l - 1];
            }
        }
        //abs(x - a[l]) - минимально для любого l - 1 <= i <= l + 1
        return a[l];
    }
}

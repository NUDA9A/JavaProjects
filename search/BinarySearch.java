package search;

import java.util.Arrays;
import java.util.List;

// :NOTE: не забываем убирать лишние импорты

public class BinarySearch {
    //Pred: args.length > 0
    //Post: 0 <= R <= args.length
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

        //Pred: args[i] - число - инвариант
        //Post: a - заполненный массив чисел размера args.length - 1
        while (i < args.length) {
            //Pred: Integer.parseInt(args[i]) == Integer.parseInt(args[i]) && args[i] - число
            //Post: curr == Integer.parseInt(args[i])
            int curr = Integer.parseInt(args[i]);

            //Pred: curr == curr && 0 <= i - 1 < args.length - 1
            //Post: a[i - 1] == curr
            a[i - 1] = curr;

            //Pred: sum' + curr == sum' + curr
            //Post: sum == sum' + curr
            sum += curr;

            //Pred: i' + 1 == i' + 1
            //Post: i == i' + 1
            i++;
        }

        //Pred: x - число && args.length - 1 >= 0
        //Post: вызвали функцию
        if ((sum + x) % 2 == 0) {
            System.out.println(recoursiveBinSearch(a, 0, args.length - 1, x));
        } else {
            System.out.println(iterativeBinSearch(a, 0, args.length - 1, x));
        }
    }

    //Inv: l <= r
    //Pred: 0 <= l <= r <= a.length && x - число // NOTE: ничего не скзано про массив
    //Post: 0 <= R <= a.length
    // NOTE: что возврщает функция
    public static int iterativeBinSearch(int[] a, int l, int r, int x) {

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
        return l;
    }

    //Inv: l <= r
    //Pred: 0 <= l <= r <= a.length && x - число
    //Post: 0 <= l <= a.length
    public static int recoursiveBinSearch(int[] a, int l, int r, int x) {
        //Pred: Inv
        //Post: 0 <= l <= a.length
        if (l >= r) {
            //Pred1: l == r
            //Post1: 0 <= l <= a.length
            return l;
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
                // NOTE: какие условия здесь? (-)
                return recoursiveBinSearch(a, m, r, x);
            }
        }
    }
}

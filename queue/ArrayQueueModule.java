package queue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

//Model: a[head]...a[tail] || (a[0]...a[tail] && a[head]...a[n])
//Inv: (head <= tail && forall: i=head...tail a[i] != null) || (head > tail && forall: i=0...tail, i=head...n a[i] != null)
public class ArrayQueueModule {

    private static int tail = 0, head = 0;

    private static Object[] elements = new Object[5];

    //Pred: true
    //Post: R = (tail == head) && immutable(R)
    public static boolean isEmpty() {
        return tail == head;
    }

    //Pred: true
    //Post: 0 <= R < elements.length && immutable(R)
    public static int size() {
        if (tail < head) {
            return elements.length - head + tail;
        }
        return tail - head;
    }

    //Pred: element != null && 0 <= tail < elements.length
    //Post: size'() == size() + 1 && elements[tail] == element && immutable(tail) && 0 <= tail' < elements.length
    private static void implElem(Object element) {
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    //Pred: capacity > 0 && elements != null
    //Post: elements'.length == capacity * 2 && T' == T
    private static void ensureCapacity(int capacity) {
        elements = Arrays.copyOf(elements, capacity * 2);
    }

    //Pred: elements != null && tail + 1 == head
    //Post: elements'.length == (elements.length + 1) * 2 && 0 == head <= tail
    private static void recreate() {
        tail = elements.length - 1;
        Object[] temp1 = Arrays.copyOfRange(elements, head, elements.length);
        Object[] temp2 = Arrays.copyOfRange(elements, 0, head);
        System.arraycopy(temp1, 0, elements, 0, temp1.length);
        System.arraycopy(temp2, 0, elements, temp1.length, temp2.length);
        ensureCapacity(elements.length + 1);
        head = 0;
    }

    //Pred: element != null
    //Post: size'() == size() + 1 && elements[tail] == element && immutable(tail) && 0 <= tail' < elements.length
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);

        if (tail + 1 == elements.length) {
            if (head == 0) {
                ensureCapacity(elements.length + 1);
            }
        }

        if (tail + 1 == head) {
            recreate();
        }

        implElem(element);
    }

    //Pred: 0 <= head < elements.length
    //Post: R == elements[head] && immutable(elements[head])
    public static Object element() {
        return elements[head];
    }

    //Pred: 0 <= head < elements.length
    //Post: R == elements[head] && size'() == size() - 1 && immutable(R)
    public static Object dequeue() {
        if (isEmpty()) {
            return null;
        }

        Object result = elements[head];
        head = (head + 1) % elements.length;
        return result;
    }

    //Pred: true
    //Post: size'() == 0
    public static void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }

    //Pred: pred != null
    //Post: R = count(pred.test(elements[i])) forall: i=head...tail || (i=0...tail && i=head...elements.length)
    public static int countIf(Predicate<Object> pred) {
        if (size() <= 0) {
            return 0;
        }

        int count = 0;
        for (int i = head; i != tail; i = (i + 1) % elements.length) {
            if (pred.test(elements[i])) {
                count++;
            }
        }
        return count;
    }
}

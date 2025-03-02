package queue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

//Model: a[head]...a[tail] || (a[0]...a[tail] && a[head]...a[n])
//Inv: (head <= tail && forall: i=head...tail a[i] != null) || (head > tail && forall: i=0...tail, i=head...n a[i] != null)
public class ArrayQueueADT {
    private Object[] elements;
    private int head, tail;

    //Pred: true
    //Post: elements = new Object[5] && head = tail = 0
    public ArrayQueueADT() {
        elements = new Object[5];
        head = 0;
        tail = 0;
    }

    //Pred: queueADT != null
    //Post: R = (tail == head) && immutable(R)
    public static boolean isEmpty(ArrayQueueADT queueADT) {
        return queueADT.tail == queueADT.head;
    }

    //Pred: queueADT != null
    //Post: 0 <= R < elements.length && immutable(R)
    public static int size(ArrayQueueADT queueADT) {
        if (queueADT.tail < queueADT.head) {
            return queueADT.elements.length - queueADT.head + queueADT.tail;
        }
        return queueADT.tail - queueADT.head;
    }

    //Pred: element != null && 0 <= tail < elements.length && queueADT != null
    //Post: size'() == size() + 1 && elements[tail] == element && immutable(tail) && 0 <= tail' < elements.length
    private static void implElem(ArrayQueueADT queueADT, Object element) {
        queueADT.elements[queueADT.tail] = element;
        queueADT.tail = (queueADT.tail + 1) % queueADT.elements.length;
    }

    //Pred: capacity > 0 && elements != null && queueADT != null
    //Post: elements'.length == capacity * 2 && T' == T
    private static void ensureCapacity(ArrayQueueADT queueADT, int capacity) {
        queueADT.elements = Arrays.copyOf(queueADT.elements, capacity * 2);
    }

    //Pred: elements != null && tail + 1 == head && queueADT != null
    //Post: elements'.length == (elements.length + 1) * 2 && 0 == head <= tail
    private static void recreate(ArrayQueueADT queueADT) {
        queueADT.tail = queueADT.elements.length - 1;
        Object[] temp1 = Arrays.copyOfRange(queueADT.elements, queueADT.head, queueADT.elements.length);
        Object[] temp2 = Arrays.copyOfRange(queueADT.elements, 0, queueADT.head);
        System.arraycopy(temp1, 0, queueADT.elements, 0, temp1.length);
        System.arraycopy(temp2, 0, queueADT.elements, temp1.length, temp2.length);
        ensureCapacity(queueADT, queueADT.elements.length + 1);
        queueADT.head = 0;
    }

    //Pred: element != null && queueADT != null
    //Post: size'() == size() + 1 && elements[tail] == element && immutable(tail) && 0 <= tail' < elements.length
    public static void enqueue(ArrayQueueADT queueADT, Object element) {
        Objects.requireNonNull(element);

        if (queueADT.tail + 1 == queueADT.elements.length) {
            if (queueADT.head == 0) {
                ensureCapacity(queueADT, queueADT.elements.length + 1);
            }
        }

        if (queueADT.tail + 1 == queueADT.head) {
            recreate(queueADT);
        }

        implElem(queueADT, element);
    }

    //Pred: 0 <= head < elements.length && queueADT != null
    //Post: R == elements[head] && immutable(elements[head])
    public static Object element(ArrayQueueADT queueADT) {
        return queueADT.elements[queueADT.head];
    }

    //Pred: 0 <= head < elements.length && queueADT != null
    //Post: R == elements[head] && size'() == size() - 1 && immutable(R)
    public static Object dequeue(ArrayQueueADT queueADT) {
        if (isEmpty(queueADT)) {
            return null;
        }

        Object result = queueADT.elements[queueADT.head];
        queueADT.head = (queueADT.head + 1) % queueADT.elements.length;
        return result;
    }

    //Pred: queueADT != null
    //Post: size'() == 0
    public static void clear(ArrayQueueADT queueADT) {
        while (!isEmpty(queueADT)) {
            dequeue(queueADT);
        }
    }

    //Pred: pred != null && queueADT != null
    //Post: R = count(pred.test(elements[i])) forall: i=head...tail || (i=0...tail && i=head...elements.length)
    public static int countIf(ArrayQueueADT queueADT, Predicate<Object> pred) {
        if (size(queueADT) <= 0) {
            return 0;
        }

        int count = 0;
        for (int i = queueADT.head; i != queueADT.tail; i = (i + 1) % queueADT.elements.length) {
            if (pred.test(queueADT.elements[i])) {
                count++;
            }
        }
        return count;
    }
}

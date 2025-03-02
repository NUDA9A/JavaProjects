package queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

//Model: a[0...n - 1], n = size
//Inv: forall: i=0...n - 1 a[i] != null
public class ArrayQueue extends AbstractQueue {
	
	private int head = 0, tail = 0;
	private Object[] elements = new Object[5];

    //Pred: true
    //Post: R == (size == 0) && immutable(a, 0, n - 1)
    @Override
    public boolean isEmpty() {
        return tail == head;
    }

    //Pred: true
    //Post: R == size && immutable(a, 0, n - 1)
    @Override
    public int size() {
        if (tail < head) {
            return elements.length - head + tail;
        }
        return tail - head;
    }

    //Pred: element != null
    //Post: size' == size + 1 && a[n] == element && immutable(a, 0, n - 1)
    private void implElem(Object element) {
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    private void ensureCapacity(int capacity) {
        elements = Arrays.copyOf(elements, capacity * 2);
    }

    private void recreate() {
        tail = elements.length - 1;
        // :NOTE: зачем лишнее копирование в temp1, temp2?
        Object[] temp1 = Arrays.copyOfRange(elements, head, elements.length);
        System.arraycopy(elements, 0, elements, temp1.length, head);
        System.arraycopy(temp1, 0, elements, 0, temp1.length);
        ensureCapacity(elements.length + 1);
        head = 0;
    }

    //Pred: element != null
    // :NOTE: n' = n + 1, a'[n] = element, immutable(a, 0, n-1)
    //Post: n' == n + 1 && a'[n] == element && immutable(a, 0, n - 1)
    @Override
    public void enqueue(Object element) {
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

    //Pred: size > 0
    //Post: R == a[0] && immutable(a, 0, n - 1)
    @Override
    public Object element() {
        assert !isEmpty();

        return elements[head];
    }

    @Override
    public void clear() {
        elements = new Object[5];
        tail = head = 0;
    }

    //Pred: size > 0
    //Post: R == a[0] && size' == size - 1 && immutable(a, 1, n - 1)
    @Override
    public Object dequeue() {
        assert !isEmpty();

        Object result = elements[head];
        head = (head + 1) % elements.length;
        return result;
    }

    //Pred: pred != null
    //Post: R = count(pred.test(a[i])) forall: i=0...n - 1 && immutable(a, 0, n - 1)
    public int countIf(Predicate<Object> pred) {
        assert pred != null;

        int count = 0;
        for (int i = head; i != tail; i = (i + 1) % elements.length) {
            if (pred.test(elements[i])) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected ArrayList<Object> getReformedValues() {
        ArrayList<Object> result = new ArrayList<>();
        result.add(elements[head]);
        for (int i = (head + 1) % elements.length; i != tail; i = (i + 1) % elements.length) {
            if (!result.get(result.size() - 1).equals(elements[i])) {
                result.add(elements[i]);
            }
        }
        return result;
    }
}

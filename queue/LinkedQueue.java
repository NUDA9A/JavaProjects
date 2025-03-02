package queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LinkedQueue extends AbstractQueue {
    private LinkedItem tail, head;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void enqueue(Object x) {
        Objects.requireNonNull(x);

        LinkedItem element = tail;
        tail = new LinkedItem(x, null);
        if (size == 0) {
            head = tail;
        } else {
            element.next = tail;
        }

        size++;
    }

    @Override
    public Object dequeue() {
        assert !isEmpty();

        LinkedItem element = head.next;
        size--;
        Object result = head.getValue();
        head = element;

        return result;
    }

    @Override
    public Object element() {
        return head.getValue();
    }

    @Override
    public void clear() {
        size = 0;
        head = tail = null;
    }

    @Override
    protected ArrayList<Object> getReformedValues() {
        ArrayList<Object> result = new ArrayList<>();
        LinkedItem curr = head;
        result.add(curr.getValue());
        curr = curr.next;
        while (curr.next != null) {
            if (!result.get(result.size() - 1).equals(curr.getValue())) {
                result.add(curr.getValue());
            }
            curr = curr.next;
        }
        if (!result.get(result.size() - 1).equals(curr.getValue())) {
            result.add(curr.getValue());
        }
        return result;
    }

    private class LinkedItem {
        private final Object value;

        private LinkedItem next;

        private LinkedItem(Object value, LinkedItem next) {
            this.value = value;
            this.next = next;
        }

        private Object getValue() {
            return this.value;
        }
    }
}

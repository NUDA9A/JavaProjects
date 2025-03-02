package queue;

import java.util.ArrayList;

public abstract class AbstractQueue implements Queue {
    // :NOTE: head/tail не используется в LinkedQueue

    // :NOTE: это детали реализации очереди на циклическом массиве

    @Override
    public abstract int size();

    @Override
    public abstract boolean isEmpty();

    @Override
    public abstract void enqueue(Object x);

    @Override
    public abstract Object dequeue();

    @Override
    public abstract Object element();

    @Override
    public abstract void clear();

    protected abstract ArrayList<Object> getReformedValues();

    @Override
    public void dedup() {
        if (size() <= 1) {
            return;
        }

        ArrayList<Object> reformedQueue = getReformedValues();
        clear();
        for (Object value : reformedQueue) {
            enqueue(value);
        }
    }
}

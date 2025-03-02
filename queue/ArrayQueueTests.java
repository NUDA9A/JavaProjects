package queue;

public class ArrayQueueTests {
    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        ArrayQueueADT queueADT = new ArrayQueueADT();

        System.out.println("ArrayQueue Tests:");
        for (int i = 0; i < 5; i++) {
            queue.enqueue("1e" + i);
        }

        while (!queue.isEmpty()) {
            System.out.println("element: " + queue.element() + " size: " + queue.size() + " dequeued: " + queue.dequeue());
        }
        System.out.println("ArrayQueueModule Tests:");

        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue("2e" + i);
        }

        while (!ArrayQueueModule.isEmpty()) {
            System.out.println("element: " + ArrayQueueModule.element() + " size: " + ArrayQueueModule.size() + " dequeued: " + ArrayQueueModule.dequeue());
        }

        System.out.println("ArrayQueueADT Tests:");
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queueADT, "3e" + i);
        }

        while (!ArrayQueueADT.isEmpty(queueADT)) {
            System.out.println("element: " + ArrayQueueADT.element(queueADT) + " size: " + ArrayQueueADT.size(queueADT) + " dequeued: " + ArrayQueueADT.dequeue(queueADT));
        }
    }
}

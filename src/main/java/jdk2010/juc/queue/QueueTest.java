package jdk2010.juc.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedBlockingQueue<>(2);
        queue.add("a");
        queue.add("a");
        queue.add("2");
        queue.offer("2");
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.size());
    }
}

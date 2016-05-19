package jdk2010.juc.queue;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest {

    public static void main(String[] args) {
        final ArrayBlockingQueue<Integer> test = new ArrayBlockingQueue<Integer>(10);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        
                        System.out.println("塞数据");
                        if(test.size()==10)
                        {
                                System.out.println("数据已满，等待取数据");
                        }
                        test.put(100);
                       
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(test.size()==0)
                        {
                                System.out.println("数据空，等待放数据");
                        }
                        System.out.println("取数据");
                        test.take();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}

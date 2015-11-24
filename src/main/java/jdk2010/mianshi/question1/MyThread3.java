package jdk2010.mianshi.question1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyThread3 extends Thread {
    private static int currentCount = 0;
    private static String currentName = "A";
    Lock lock = new ReentrantLock();
    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();

    public MyThread3(String name) {

        this.setName(name);

    }

    @Override
    public void run() {
        for (int i = 0; i < 30;) {
            lock.lock();
                if (i % 3 == 0) {
                    System.out.println(i + "A");
                    currentName = "B";
                    i++;
                    System.out.println("Anotify");
                    c2.signal();
                } else {
                    try {
                        c1.await();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (i % 3 == 1) {
                    System.out.println("B");
                    currentName = "C";
                    i++;
                    System.out.println("Bnotify");
                    c3.signal();
                } else {

                    try {
                        c2.wait();
                        System.out.println("Bwait");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (i % 3 == 2) {
                    System.out.println("C");
                    i++;
                    currentName = "A";
                    System.out.println("Cnotify");
                    c1.signal();
                } else {
                    try {
                        c3.wait();
                        System.out.println("Cwait");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

        }

    }

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();

        new MyThread3("A").start();
        new MyThread3("B").start();
        new MyThread3("C").start();

    }

}

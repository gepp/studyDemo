package jdk2010.mianshi.question1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Function2 {
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    static String str = "A";

    static int count = 100000;

    public static void main(String[] args) throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        ThreadGroup tg = new ThreadGroup("test");
        Thread thread1;
        Thread thread2;
        Thread thread3;
        thread1 = new Thread(tg, new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    for (int i = 0; i < count;) {
                        if (str == "A") {
                            System.out.println(i + "A");
                            str = "B";
                            i++;
                            condition.signalAll();
                        } else {
                            condition.await();
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    lock.unlock();
                }
            }
        });

        thread2 = new Thread(tg, new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    for (int i = 0; i < count;) {
                        if (str == "B") {
                            System.out.println(i + "B");
                            str = "C";
                            i++;
                            condition.signalAll();
                        } else {
                            condition.await();
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    lock.unlock();
                }
            }
        });

        thread3 = new Thread(tg, new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    for (int i = 0; i < count;) {
                        if (str == "C") {
                            System.out.println(i + "C");
                            str = "A";
                            i++;
                            condition.signalAll();
                        } else {
                            condition.await();
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    lock.unlock();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        while (true) {
            if (tg.activeCount() == 0) {
                long endTime = System.currentTimeMillis();
                System.out.println("need :" + (endTime - beginTime) + "ms");
                break;
            } else {
                Thread.sleep(100);
            }
        }
    }
}

package jdk2010.current.reentrant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantTryRunnable implements Runnable {
    ReentrantLock lock = null;

    public ReentrantTryRunnable() {
        lock = new ReentrantLock();// 默认false
    }

    public void write(String name) throws InterruptedException {

        System.out.println("running" + name);
        try {

            System.out.println("activeCount:" + name + "-->" + Thread.activeCount());

            System.out.println("tryLock 500" + name + ":" + lock.tryLock(1, TimeUnit.SECONDS));
            // lock.lock();
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                Thread.sleep(4000);
                System.out.println("test" + name);

            } else {
                System.out.println("停止当前lock" + name);
            }
        } catch (Exception e) {
            System.out.println("报错了");
        }

        finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                System.out.println("message:" + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try {
            write(Thread.currentThread().getName());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

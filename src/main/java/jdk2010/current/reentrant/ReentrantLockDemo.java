package jdk2010.current.reentrant;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
     ReentrantLock lock = new ReentrantLock();

    public  void lock() {
        boolean lockReturn = lock.tryLock();
        System.out.println("lockReturn1:" + lockReturn);
        if (lockReturn) {
            try {
                System.out.println("进入lock");
            } catch (Exception e) {

            } finally {
                lock.unlock();
            }
        }
    }

    public  void lockTime() throws InterruptedException {
        boolean lockReturn = lock.tryLock(7, TimeUnit.SECONDS);
        System.out.println("lockReturn2:" + lockReturn);
        if (lockReturn) {
            try {
                System.out.println("进入lockTime");
            } catch (Exception e) {
                System.out.println("超时");
            } finally {

                lock.unlock();
            }
        }
    }

    public void test1() throws InterruptedException {
        ReentrantLockDemo demo=new ReentrantLockDemo();
        demo.lock();
        demo.lockTime();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("lock.lock");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();
        Thread.sleep(100);
        demo.lock();
        demo.lockTime();
    }
    
    public void testReentry() {  
        lock.lock();  
        Calendar now = Calendar.getInstance();  
        System.out.println(now.getTime() + " " + Thread.currentThread() + " get lock.");  
    }  
    public ReentrantLock getLock() {  
        return lock;  
    }  
    public void test2(){
        testReentry();
        testReentry();
        testReentry();
        getLock().unlock();
        getLock().unlock();
        getLock().unlock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean lockReturn = lock.tryLock();
                    System.out.println("lockReturn:"+lockReturn);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }  
            }
        }).start();
    }
    

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo demo=new ReentrantLockDemo();
        demo.test2();
    }
}

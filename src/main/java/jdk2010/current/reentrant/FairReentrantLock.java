package jdk2010.current.reentrant;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairReentrantLock {
    ReentrantLock  lock = new ReentrantLock(true);

    public void doMethod() {
        lock.lock();
        try {
            System.out.println("�߳�"+Thread.currentThread().getName() + "��ȡ��lock");
            System.out.println("getHoldCount = " + lock.getQueueLength());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
       final FairReentrantLock fair=new FairReentrantLock();
        for(int i=0;i<10;i++){
          Thread t=  new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("�߳�" + Thread.currentThread().getName() + "������");
                            fair.doMethod();
                        }
                    }
                    );
             t.setName("thread"+i);
             t.start();
        }
    }
}

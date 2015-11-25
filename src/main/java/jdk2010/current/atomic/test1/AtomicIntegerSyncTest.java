package jdk2010.current.atomic.test1;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerSyncTest {

    int count = 0;
    
    static int maxCount=1000000;

    private AtomicInteger acount = new AtomicInteger(0);

    public int setCount() {
        synchronized (this) {
            if(getCount()<maxCount){
            return ++count;
            }
            else{
                return count;
            }
        }
    }

    public int getCount() {
        synchronized (this) {
            return count;
        }
    }
    public int setAtomicCount() {
        if(getAtomicCount()<maxCount){
        return acount.incrementAndGet();
        }else{
            return getAtomicCount();
        }
    }

    public int getAtomicCount() {
        return acount.get();
    }
    
    
    

    public static void main(String[] args) throws InterruptedException {
         testSync();
        Thread.sleep(3000);
        testAtomic();
    }

    public static void testSync() throws InterruptedException {
        for (int k = 0; k < 1; k++) {
            long starttime = System.currentTimeMillis();
            AtomicIntegerSyncTest test = new AtomicIntegerSyncTest();
            ThreadGroup tg = new ThreadGroup("test");
            for (int i = 0; i < 10; i++) {
                new Thread(tg, new SyncThread(test, starttime,maxCount)).start();
            }
            // if (tg.activeCount() != 0) {
            // System.out.println(tg.activeCount());
            // Thread.sleep(1);
            // }
            // if (tg.activeCount() == 0) {
            // long endtime = System.currentTimeMillis();
            // System.out.println("ºÄÊ±£º" + (endtime - starttime) + " MaxCount:" + test.getCount());
            // }
        }
    }

    public static void testAtomic() throws InterruptedException {
        for (int k = 0; k < 1; k++) {
            long starttime = System.currentTimeMillis();
            ThreadGroup tg = new ThreadGroup("test");
            AtomicIntegerSyncTest test = new AtomicIntegerSyncTest();

            for (int i = 0; i < 10; i++) {
                new Thread(tg, new AtomicThread(test,starttime,maxCount)).start();
            }
            // if (tg.activeCount() != 0) {
            // System.out.println(tg.activeCount());
            // Thread.sleep(1);
            // }
            // if (tg.activeCount() == 0) {
            // long endtime = System.currentTimeMillis();
            // System.out.println("ºÄÊ±£º" + (endtime - starttime) + " MaxCount:" + test.getCount());
            // }
        }
    }
}

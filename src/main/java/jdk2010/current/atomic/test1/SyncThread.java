package jdk2010.current.atomic.test1;

public class SyncThread implements Runnable {

    int maxCount;
    long starttime;
    AtomicIntegerSyncTest test;

    public SyncThread(AtomicIntegerSyncTest test, long starttime, int maxCount) {
        this.test = test;
        this.starttime = starttime;
        this.maxCount = maxCount;
    }

    @Override
    public void run() {
        int v = test.getCount();
        while (v < maxCount) {
            v = test.setCount();
        }
        long endtime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "====SyncThread spend:" + (endtime - starttime) + "ms"
                + " v=" + v);

    }

}

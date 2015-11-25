package jdk2010.current.atomic.test1;


public class AtomicThread implements Runnable {

    private long starttime;
    int maxCount;
    AtomicIntegerSyncTest test;

    public AtomicThread(AtomicIntegerSyncTest test, long starttime, int maxCount) {
        this.test = test;
        this.starttime = starttime;
        this.maxCount = maxCount;
    }

    @Override
    public void run() {
        
        int v = test.getAtomicCount();
        while (v < maxCount) {
            v = test.setAtomicCount();
        }
        long endtime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "====AtomicThread spend:" + (endtime - starttime) + "ms"
                + " v=" +v);

    }

}

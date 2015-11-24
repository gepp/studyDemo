package jdk2010.current.atomic.test1;

public class AtomicIntegerSyncTest {

    int count = 0;

    public void add() {
        synchronized (this) {
            count++;
            // System.out.println(Thread.currentThread().getName() + "-add-" + getMaxCount());
        }
    }

    public  int getCount() {
        synchronized(this){
        return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        testSync();
    }

    public static void testSync() throws InterruptedException {
        for (int k = 0; k < 100; k++) {
            long starttime = System.currentTimeMillis();
            AtomicIntegerSyncTest test = new AtomicIntegerSyncTest();
            ThreadGroup tg = new ThreadGroup("test");
            for (int i = 0; i < 10; i++) {
                new Thread(tg, new SyncThread(test)).start();
            }
            if (tg.activeCount() != 0) {
                Thread.sleep(1);
            }
            long endtime = System.currentTimeMillis();
            System.out.println("耗时：" + (endtime - starttime) + " MaxCount:" + test.getCount());
        }
    }
}

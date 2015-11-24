package jdk2010.current.atomic.test1;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicThread implements Runnable {

    int maxCount;

    int count = 0;
    
    AtomicInteger intg=new AtomicInteger(count);

    public AtomicThread(int maxCount) {
        maxCount = maxCount;
    }

    @Override
    public void run() {
        long starttime = System.currentTimeMillis();
        while (count < maxCount) {
            intg.incrementAndGet();
        }
        long endtime = System.currentTimeMillis();
        System.out.println("耗时：" + (endtime - starttime));

    }

}

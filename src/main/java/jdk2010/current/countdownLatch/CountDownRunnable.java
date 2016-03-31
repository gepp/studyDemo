package jdk2010.current.countdownLatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

class CountDownRunnable implements Runnable {
    private CountDownLatch latch;
    
    
    private int workTime;

    public CountDownRunnable(int workTime, CountDownLatch latch) {
        this.latch = latch;
        this.workTime = workTime;
    }

    @Override
    public void run() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(Thread.currentThread().getName() + "����ʱ�䣺" + format.format(new Date()));
        try {
            Thread.sleep(workTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

}

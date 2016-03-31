package jdk2010.current.countdownLatch;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
public class CountDownLatchMain {
    
    
    
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch=new CountDownLatch(3);
        CountDownRunnable r1=new CountDownRunnable(300,latch);
        CountDownRunnable r2=new CountDownRunnable(400,latch);
        CountDownRunnable r3=new CountDownRunnable(1500,latch);
        Thread t1=new Thread(r1);
        Thread t2=new Thread(r2);
        Thread t3=new Thread(r3);
        t1.start();
        t2.start();
        t3.start();
        latch.await();//等待所有工人完成工作  
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("all work done at "+format.format(new Date()));  
    }
}

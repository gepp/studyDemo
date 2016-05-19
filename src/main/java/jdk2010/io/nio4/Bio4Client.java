package jdk2010.io.nio4;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bio4Client {

    

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        long start=System.currentTimeMillis();
        int totalCount=5;
        CountDownLatch latch=new CountDownLatch(totalCount);
        ExecutorService service=Executors.newFixedThreadPool(5);
        for(int i=0;i<totalCount;i++){
            service.execute(new BioRunnable(latch,9999));
        }
        latch.await();
        long end=System.currentTimeMillis();
        System.out.println("ºÄÊ±£º"+(end-start));
        service.shutdown();
    }

}

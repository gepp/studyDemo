package jdk2010.juc.synchronousQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCachedThreadPool {
    public static void main(String[] args) {

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 1; i < 8000; i++)
            es.submit(new Task());

    }

}

class Task implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

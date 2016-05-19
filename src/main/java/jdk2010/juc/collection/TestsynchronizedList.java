package jdk2010.juc.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestsynchronizedList {
    private static List<String> TEST_LIST = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(95);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TEST_LIST.add("11");
                    System.out.println("Thread1 running");
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(98);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TEST_LIST.add("22");
                    System.out.println("Thread2 running");
                }
            }
        }).start();
    }
}

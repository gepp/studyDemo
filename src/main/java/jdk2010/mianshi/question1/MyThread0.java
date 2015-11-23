package jdk2010.mianshi.question1;

import java.util.Map;

public class MyThread0 extends Thread {

    private static int currentCount = 0;

    public MyThread0(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        while (currentCount < 30) {
            switch (currentCount % 3) {
                case 0:
                    if ("A".equals(getName())) {
                        System.out.println(currentCount + getName());
                        if ("C".equals(getName())) {
                            System.out.println();
                        }
                        currentCount++;
                    }
                    break;
                case 1:
                    if ("B".equals(getName())) {
                        System.out.println(getName());
                        if ("C".equals(getName())) {
                            System.out.println();
                        }
                        currentCount++;
                    }
                    break;
                case 2:
                    if ("C".equals(getName())) {
                        System.out.println(getName());
                        if ("C".equals(getName())) {
                            System.out.println();
                        }
                        currentCount++;
                    }
                    break;
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();
        new MyThread0("A").start();
        new MyThread0("B").start();
        new MyThread0("C").start();

    }

}

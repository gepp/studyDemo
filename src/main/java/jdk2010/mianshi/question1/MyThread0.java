package jdk2010.mianshi.question1;

public class MyThread0 extends Thread {

    private static int currentCount = 0;

    public MyThread0(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        while (currentCount < 30) {
             System.out.println("========"+Thread.currentThread().getName());
            if (currentCount % 3 == 0) {
                if ("A".equals(getName())) {
                    System.out.println("A");
                    currentCount++;
                }
            }
            if (currentCount % 3 == 1) {
                if ("B".equals(getName())) {
                    System.out.println("B");
                    currentCount++;
                }
            }
            if (currentCount % 3 == 2) {
                if ("C".equals(getName())) {
                    System.out.println("C");
                    System.out.println("");
                    currentCount++;
                }
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {

        new MyThread0("A").start();
        new MyThread0("B").start();
        new MyThread0("C").start();

    }

}

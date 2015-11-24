package jdk2010.mianshi.question1;

public class MyThread1 extends Thread {
    private static int currentCount = 0;
    private static String currentName = "A";
    static Object lock = new Object();

    public MyThread1(String name) {

        this.setName(name);

    }

    @Override
    public void run() {
        for (int i = 0; i < 30;) {
            synchronized (lock) {
                if (i % 3 == 0) {
                    System.out.println(i + "A");
                    currentName = "B";
                    i++;
                    System.out.println("Anotify");
                    lock.notifyAll();
                } else {
                    try {
                        lock.wait();
                        System.out.println("Await");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                if (i % 3 == 1) {
                    System.out.println("B");
                    currentName = "C";
                    i++;
                    System.out.println("Bnotify");
                    lock.notifyAll();
                } else {

                    try {
                        lock.wait();
                        System.out.println("Bwait");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (i % 3 == 2) {
                    System.out.println("C");
                    i++;
                    currentName = "A";
                    System.out.println("Cnotify");
                    lock.notifyAll();
                } else {
                    try {
                        lock.wait();
                        System.out.println("Cwait");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

        }

    }

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();

        new MyThread1("A").start();
        new MyThread1("B").start();
        new MyThread1("C").start();

    }

}

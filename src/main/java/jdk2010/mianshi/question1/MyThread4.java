package jdk2010.mianshi.question1;

public class MyThread4 extends Thread {

    private static String currentName="A";
    
    private static Object lock=new Object();

    private  int count = 0;

    public MyThread4(String name) {
        setName(name);
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (count < 10) {
                if (!currentName.equals(getName())) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(count+currentName);
                if ("A" == currentName) {
                    currentName = "B";
                } else if ("B" == currentName) {
                    currentName = "C";
                } else if ("C" == currentName) {
                    currentName = "A";
                }
                count++;
                lock.notifyAll();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();
        
        new MyThread4("A").start();
        new MyThread4("B").start();
        new MyThread4("C").start();

    }

}

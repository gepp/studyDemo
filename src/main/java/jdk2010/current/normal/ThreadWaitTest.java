package jdk2010.current.normal;

public class ThreadWaitTest extends Thread{

	@Override
	public void run(){
		System.out.println("t1---start");
		synchronized (this) {
            System.out.println(Thread.currentThread().getName()+" call notify()");
            notify();
        }
	}
	
}

package jdk2010.current.normal;

public class ThreadWaitMain {
	public static void main(String[] args) throws InterruptedException {
		ThreadWaitTest t1=new ThreadWaitTest();
		 synchronized(t1) {
	            // 启动“线程t1”
				//System.out.println(Thread.currentThread().getName()+" start t1");
				t1.start();
				t1.join();
				for(int i=0;i<100;i++)
				System.out.println("main---");
				// 主线程等待t1通过notify()唤醒。
 				//t1.wait();

				System.out.println(Thread.currentThread().getName()+" continue");
	        }
	}
}

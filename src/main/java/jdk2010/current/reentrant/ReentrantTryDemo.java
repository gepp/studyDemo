package jdk2010.current.reentrant;


public class ReentrantTryDemo {
	 
	public static void main(String[] args) throws InterruptedException {
		ReentrantTryRunnable reentrantRunnable=new ReentrantTryRunnable();
		 Thread a=new Thread(reentrantRunnable);
		 Thread b=new Thread(reentrantRunnable);
		 a.start();
		 System.out.println("aaa");
 		 Thread.sleep(2000);
 		 
  		 b.start();
		
		// a.interrupt();
	}
}

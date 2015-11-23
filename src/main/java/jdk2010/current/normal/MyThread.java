package jdk2010.current.normal;

public class MyThread extends Thread{
		@Override
		public void run(){
			synchronized (this) {
				for(int i=0;i<5;i++){
					System.out.println(Thread.currentThread().getName()+"-->"+i);
				}
			}
			
		}
		
}

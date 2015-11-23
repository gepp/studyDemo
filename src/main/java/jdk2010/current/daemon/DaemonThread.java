package jdk2010.current.daemon;

public class DaemonThread  implements Runnable{
	
	private int index=0;
	
	@Override
	public void run() {
		while(index<100){
			System.out.println("daemon running..."+index);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("daemon dead");
			}
			index++;
		}
	}
	
}

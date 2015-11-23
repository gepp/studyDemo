package jdk2010.current.volatiletest;

public class VolatileRunnable implements Runnable{

	private volatile int count=0;
	
	@Override
	public void run() {
		count++;
	}
	public int getCount(){
		return count;
	}

}

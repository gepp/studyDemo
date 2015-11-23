package jdk2010.current.volatiletest;

public class SyncRunnable implements Runnable{

	private  int count=0;
	
	@Override
	public void run() {
		synchronized (this) {
			count++;
		}
		
	}
	public int getCount(){
		return count;
	}

}

package jdk2010.current.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicRunnable implements Runnable{

	private AtomicInteger count=new AtomicInteger(0);
	
	@Override
	public void run() {
		count.incrementAndGet();
	}
	public int getCount(){
		return count.intValue();
	}

}

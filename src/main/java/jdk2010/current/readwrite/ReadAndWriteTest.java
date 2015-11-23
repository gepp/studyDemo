package jdk2010.current.readwrite;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadAndWriteTest {

	ReadWriteLock lock = new ReentrantReadWriteLock();
	Lock writeLock = lock.writeLock();
	Lock readLock = lock.readLock();

	public void write() throws InterruptedException {
		writeLock.lock();
		Thread.sleep(1);
		try {
			System.out.println(Thread.currentThread().getName() + "write");
		} finally {
			writeLock.unlock();
		}
	}

	public void read() {
		readLock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + "read");
		} finally {
			readLock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		final ReadAndWriteTest test=new ReadAndWriteTest();
		for(int i=0;i<10;i++){
 		new Thread(new Runnable() {
			@Override
			public void run() {
 				try {
					test.write();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
 			}
		}).start();	
		 
		} 
		 
		
	}
}

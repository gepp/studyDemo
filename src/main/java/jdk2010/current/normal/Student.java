package jdk2010.current.normal;

public class Student {
	public void sayHello(){
		for(int i=0;i<100;i++)
		System.out.println(Thread.currentThread().getName()+":sayHello");
	}
	
	public static synchronized void  sayBye(){
		for(int i=0;i<10;i++)
		System.out.println(Thread.currentThread().getName()+":sayBye");
	}

	public void sayYes(){
		synchronized (this) {
			for(int i=0;i<10;i++)
			System.out.println(Thread.currentThread().getName()+":sayYes");
		}
	}
	
	public void sayNo(){
		synchronized (this) {
			for(int i=0;i<10;i++)
			System.out.println(Thread.currentThread().getName()+":sayNo");
		}
		
	}
	
}

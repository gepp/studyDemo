package jdk2010.current.normal;

public class MyThreadMain {
	public static void main(String[] args) {
	
//		MyThread thread=new MyThread();
//		System.out.println("���̣߳�"+Thread.currentThread().getName());
//		System.out.println("���߳�:"+thread.getName());
//		//		 thread.run();
//		//1. run()�����������ĵ���û�κβ�ͬ,main������˳��ִ������,����ӡ�����һ��
//		thread.setDaemon(true);
//		thread.start();
//		System.out.println("abc");
//		System.out.println("���̣߳�"+Thread.currentThread().getName());
//		System.out.println("���߳�:"+thread.getName());
		
		Thread t1=new MyThread();
		Thread t2=new MyThread();
		t1.start();
		t2.start();
	}
}

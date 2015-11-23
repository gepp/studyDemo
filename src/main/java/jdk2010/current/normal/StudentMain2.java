package jdk2010.current.normal;

public class StudentMain2 {
	public static void main(String[] args) {
		final Student s = new Student();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				s.sayBye();
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				s.sayHello();
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				s.sayYes();
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				s.sayNo();
			}
		}).start();
		
		 

		

	}
}

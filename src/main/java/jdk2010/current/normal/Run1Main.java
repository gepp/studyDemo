package jdk2010.current.normal;

public class Run1Main {
	public static void main(String[] args) {
		RunTest1 test=new RunTest1("Ïß³Ì");
		Thread t1=new  Thread(test);
		Thread t2=new Thread(test);
		t1.start();
		t2.start();
	}
}

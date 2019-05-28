package jdk2010.lang.instrumentation;

public class TestMainInJar {
	public static void main(String[] args) {
		System.out.println("TestMainInJar main()");
		new TransClass().sayHello();
	}
}
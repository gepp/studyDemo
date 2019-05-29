package jdk2010.lang.reflect;

public class Foo {
	private int tryBlock;
	private int catchBlock;
	private int finallyBlock;
	private int methodExit;
	
	private String a;
	private double b;
	private Foo c;
	private boolean d;

	public void test() {
		try {
			tryBlock = 0;
		} catch (Exception e) {
			catchBlock = 1;
		} finally {
			finallyBlock = 2;
		}
		methodExit = 3;
	}
	
	
}

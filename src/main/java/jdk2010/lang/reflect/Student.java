package jdk2010.lang.reflect;

public class Student {

	private int id;

	private String name;

	public Student(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int calc() {
		int a = 500;
		int b = 200;
		int c = 50;
		int d = 30;
		long e = 1000L;
		return (a + b) / c;
	}

	public synchronized void add1() {
		id++;
	}

	public void add2() {
		synchronized (this) {
			id++;
		}
	}
}

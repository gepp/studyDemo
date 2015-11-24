package jdk2010.serial;

import java.io.Serializable;

public class StudentSerial implements Serializable {

	private static final long serialVersionUID = 3082689062208188229L;

	private int age;
	private String name;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

package jdk2010.current.atomic;

public class Student {
	private  int a;
	public Student(int a){
		this.a=a;
	}
	public int getA(){
		return a;
	}
	public String toString(){
		return "a:"+a;
	}
}

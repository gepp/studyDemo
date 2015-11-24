package jdk2010.current.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
	public static void main(String[] args) {
		Student s1=new Student(101);
		Student s2=new Student(102);
		AtomicReference<Student> reference=new AtomicReference<Student>(s1);
		// 通过CAS设置reference。如果reference的值为s1的话，则将其设置为s2
		reference.compareAndSet(s1, s2);
		Student s3 = (Student)reference.get();
		System.out.println(s1.getA());
		System.out.println(s2.getA());
		System.out.println(s3.getA());
		System.out.println(s3.equals(s1));
		System.out.println(s3==(s1));
	}
}

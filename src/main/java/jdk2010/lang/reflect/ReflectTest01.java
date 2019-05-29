package jdk2010.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectTest01 {
	public static void target(int i) {
		new Exception("#" + i).printStackTrace();
	}

	public static void test0() throws Exception {
		Class<?> klass = Class.forName("jdk2010.lang.reflect.ReflectTest01");
		Method method = klass.getMethod("target", int.class);
		method.invoke(null, 0);
	}

	public static void test1() throws Exception {
		Class<?> klass = Class.forName("jdk2010.lang.reflect.ReflectTest01");
		Method method = klass.getMethod("target", int.class);
		for (int i = 0; i < 20; i++) {
			method.invoke(null, i);
		}
	}

	public static void main(String[] args) throws Exception {
		test1();
	}
}

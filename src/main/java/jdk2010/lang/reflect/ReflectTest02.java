package jdk2010.lang.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import com.esotericsoftware.reflectasm.MethodAccess;

public class ReflectTest02 {
	public static void main(String[] args) throws Throwable {

		Student s1 = new Student(1, "gpp");

		Class<?>[] parameterTypes = new Class[] {};

		long start = System.currentTimeMillis();

		for (int i = 0; i < 100000000; i++) {

			s1.getName();

		}
		long end = System.currentTimeMillis();

		System.out.println("直接调用 100000000 times using:" + (end - start) + "ms");

		// start = System.currentTimeMillis();
		//
		// for (int i = 0; i < 100000000; i++) {
		//
		// Method m1 = Student.class.getMethod("getName", parameterTypes);
		//
		// String name = (String) m1.invoke(s1, null);
		//
		// }
		// end = System.currentTimeMillis();
		//
		// System.out.println("原生反射(没有缓存Method) 100000000 times using:" + (end -
		// start) + "ms");

		start = System.currentTimeMillis();

		Method m1 = Student.class.getMethod("getName", parameterTypes);
		// 关闭权限检查
		m1.setAccessible(true);

		for (int i = 0; i < 100000000; i++) {

			String name = (String) m1.invoke(s1, null);

		}
		end = System.currentTimeMillis();

		System.out.println("原生反射(缓存Method) 100000000 times using:" + (end - start) + "ms");

		start = System.currentTimeMillis();

		MethodAccess ma = MethodAccess.get(s1.getClass());

		int index = ma.getIndex("getName");

		for (int i = 0; i < 100000000; i++) {

			ma.invoke(s1, index, null);

		}
		end = System.currentTimeMillis();

		System.out.println("reflectasm反射(缓存Method) 100000000 times using:" + (end - start) + "ms");

		MethodHandles.Lookup lookup = MethodHandles.lookup();

		MethodHandle methodHandle = lookup.findVirtual(Student.class, "getName", MethodType.methodType(String.class));
		
		MethodHandle methodHandle2 = lookup.findVirtual(Student.class, "setName", MethodType.methodType(void.class, String.class));

		start = System.currentTimeMillis();

		for (int i = 0; i < 100000000; i++) {

			methodHandle.invoke(s1);
//			methodHandle2.invokeExact(s1);
//			methodHandle2.invokeExact(s1,"aaa");
			// methodHandle.invokeExact();

		}
		end = System.currentTimeMillis();

		System.out.println("MethodHandle反射(缓存Method) 100000000 times using:" + (end - start) + "ms");

	}
}

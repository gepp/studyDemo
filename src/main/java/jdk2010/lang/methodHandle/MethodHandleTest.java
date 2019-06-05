package jdk2010.lang.methodHandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleTest {

	class GrandFather {
		void thinking() throws Throwable {
			System.out.println("I 'm grandFather!");
		}
	}

	class Father extends GrandFather {
		void thinking() throws   Throwable {
			System.out.println("I 'm father!");
		}
	}

	class Son extends Father {
		void thinking() throws Throwable {
			MethodType mt = MethodType.methodType(void.class);
			MethodHandle md = MethodHandles.lookup().findSpecial(GrandFather.class, "thinking", mt, this.getClass());
			md.invoke(this);
		}
	}

	public static void main(String[] args) throws Throwable {
		MethodHandleTest.Son son = new MethodHandleTest().new Son();
		son.thinking();
	}
}
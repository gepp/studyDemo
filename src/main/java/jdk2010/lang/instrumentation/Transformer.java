package jdk2010.lang.instrumentation;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Transformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		// TODO Auto-generated method stub
		System.out.println("transform()");
		if (className.equals("wqz/zoom/test/TransClass")) {
			ClassPool classPool = ClassPool.getDefault();

			try {
				CtClass class1 = classPool.get(className.replaceAll("/", "."));
				CtMethod ctMethod = class1.getDeclaredMethod("sayHello");
				if (!ctMethod.isEmpty()) {
					ctMethod.insertBefore("System.out.println(\"before hello!!!\");");
				}
				return class1.toBytecode();
			} catch (NotFoundException | CannotCompileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
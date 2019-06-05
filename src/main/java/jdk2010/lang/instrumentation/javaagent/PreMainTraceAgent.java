package jdk2010.lang.instrumentation.javaagent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class PreMainTraceAgent {
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("agentArgs:" + agentArgs);
		inst.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				System.out.println("load class:" + className);
				return classfileBuffer;
			}
		});
	}
}

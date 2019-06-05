package jdk2010.lang.instrumentation.javaagent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class PreMainAddTimesAgent {
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("agentArgs:" + agentArgs);
		inst.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

				if (className.equals("jdk2010/lang/asm/Account")) {
					System.out.println("hello Account");
					ClassReader cr = new ClassReader(classfileBuffer);
					ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
					// cr.acc
				}

				return classfileBuffer;
			}
		});
	}
}

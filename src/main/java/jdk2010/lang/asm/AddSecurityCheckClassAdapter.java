package jdk2010.lang.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddSecurityCheckClassAdapter extends ClassVisitor {
	public AddSecurityCheckClassAdapter(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		System.out.println("visitMethod");
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		MethodVisitor wrappedMv = mv;
		if(mv!=null){
			if(name.equals("operation")){
				wrappedMv=new AddSecurityCheckMethodAdapter(mv);
			}
		}
		return wrappedMv;
	}

}

package jdk2010.lang.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddSecurityCheckMethodAdapter extends MethodVisitor {

	public AddSecurityCheckMethodAdapter(MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
	}

	public void visitCode() {
		System.out.println(""); 
		Label continueLabel = new Label(); 
		visitMethodInsn(Opcodes.INVOKESTATIC, "jdk2010/lang/asm/SecurityChecker", "checkSecurity", "()Z");
		visitJumpInsn(Opcodes.IFNE, continueLabel);
		visitInsn(Opcodes.RETURN);
		visitLabel(continueLabel);
		super.visitCode();
	}
}

package jdk2010.lang.instrumentation.javaagent;

import jdk2010.lang.asm.Account;

public class RunAccountMain {
	public static void main(String[] args) {
		Account account = new Account();
		account.operation();
	}
}

package jdk2010.lang.asm;

public class RunAccountMain {
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis()&0x1);
		Account account = new Account();
		account.operation();
	}
}

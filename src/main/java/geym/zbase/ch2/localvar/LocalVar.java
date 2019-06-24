package geym.zbase.ch2.localvar;

public class LocalVar {
	public void localvar1() {
		int a = 0;
		System.out.println(a);
		int b = 0;
	}

	public void localvar2() {
		{
			int a = 0;
			System.out.println(a);
		}
		int b = 0;
	}

	public void localvar3() {
		for (int i = 0; i < 100000; i++) {
			byte[] a = new byte[6 * 1024 * 1024];
		}
	}

	public static void main(String[] args) {
		LocalVar localVar=new LocalVar();
		localVar.localvar3();
	}

}

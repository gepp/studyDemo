package jdk2010.current.reentrant;

public class Producter {
	private Depot depot;

	public Producter(Depot depot) {
		this.depot = depot;
	}

	// ���Ѳ�Ʒ���½�һ���߳���ֿ���������Ʒ��
	public void add(final int val) {
		new Thread() {
			public void run() {
				System.out.println(Thread.currentThread().getName()+"����:"+val);
				depot.add(val);
			}
		}.start();
	}
}

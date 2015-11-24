package jdk2010.current.reentrant;

public class Customer {
	private Depot store;
	public Customer(Depot store){
		this.store=store;
	}
	public void subtract(final int count){
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+"ºı…Ÿ:"+count);
				store.subtract(count);
			}
		}).start();
	}
}

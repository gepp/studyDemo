package jdk2010.current.normal;

public class RunTest1  implements Runnable{

	private String name;
	
	public RunTest1(String name){
		this.name=name;
	}
	@Override
	public void run() {
		synchronized (this) {
			for(int i=0;i<5;i++){
				System.out.println(Thread.currentThread().getName()+"-->"+i);
			}
		}
		
	}

	
}

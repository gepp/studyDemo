package jdk2010.current.future;

import java.util.concurrent.Callable;

public class NumberFutureCallable implements Callable<String>{
    
	private String num;
	public NumberFutureCallable(String num){
		this.num=num;
	}
		
	@Override
	public String call() throws Exception {
		if(num.equals("2")){
			Thread.sleep(6000);
		}
		if(num.equals("6")){
			Thread.sleep(12000);
		}
		System.out.println(Thread.currentThread().getName());
		return num;
	}

}

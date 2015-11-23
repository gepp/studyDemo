package jdk2010.current.future;

import java.util.concurrent.Callable;

public class FutureCallable implements Callable<String>{

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName()+"call1start");
		Thread.sleep(8000);
		System.out.println(Thread.currentThread().getName()+"call1 running");
		return "a";
	}

}

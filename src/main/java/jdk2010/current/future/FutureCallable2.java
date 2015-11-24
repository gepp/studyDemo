package jdk2010.current.future;

import java.util.concurrent.Callable;

public class FutureCallable2 implements Callable<String>{

	@Override
	public String call() throws Exception {
		System.out.println(Thread.currentThread().getName()+"call2start");

		// TODO Auto-generated method stub
		Thread.sleep(1000);
		System.out.println(Thread.currentThread().getName()+"call2 running");
		return "b";
	}

}

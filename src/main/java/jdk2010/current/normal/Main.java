package jdk2010.current.normal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	public static void main(String[] args) {
		List<Integer> list=new CopyOnWriteArrayList<Integer>();
		ExecutorService executors=Executors.newCachedThreadPool();
		executors.execute(new Producter(list));
		executors.execute(new Customer(list));				
	}
}

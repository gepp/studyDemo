package jdk2010.util.function;

import java.util.function.Function;

public class FunctionTest {

	public static void main(String[] args) {
		FunctionTest test = new FunctionTest();
		System.out.println(test.calc(2));
		System.out.println(test.calc(2,a -> a+2));

		System.out.println(test.compute1(2,item -> item*3 ,item -> item*item ));
		
		System.out.println(test.compute2(2,item -> item*3 ,item -> item*item ));
		
	}
	
	int calc(int a ,Function<Integer,Integer> function) {
		return function.apply(a);
	}
	
	int calc(int a) {
		return a+2;
	}
	
	public int compute1(int a ,Function<Integer,Integer> f1,Function<Integer,Integer> f2) {
		return f1.compose(f2).apply(a);
	}
	
	public int compute2(int a ,Function<Integer,Integer> f1,Function<Integer,Integer> f2) {
		return f1.andThen(f2).apply(a);
	}
	
}

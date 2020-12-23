package jdk2010.util.function;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BiFunctionTest {

	public static void main(String[] args) {
		BiFunctionTest test = new BiFunctionTest();
		System.out.println(test.calc(2,3,(a,b) -> a*3));
		 
	}
	
	int calc(int a ,int b,BiFunction<Integer,Integer,Integer> function) {
		return function.apply(a,b);
	}
	
	 
	
	
}

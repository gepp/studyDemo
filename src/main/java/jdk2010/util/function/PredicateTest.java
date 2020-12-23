package jdk2010.util.function;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateTest {

	public static void main(String[] args) {
	
		Predicate<String> predicate = str -> str.length()>5;
		
		System.out.println(predicate.test("abc"));
		
		List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		PredicateTest test = new  PredicateTest();
		
		test.print(list, item -> item % 2 ==0 );
		
	}
	
	public void print(List<Integer> list,Predicate<Integer> predicate) {
		
		for(Integer i : list ) {
			if(predicate.test(i)) {
				System.out.println(i);
			}
		}
		
	}
	
}

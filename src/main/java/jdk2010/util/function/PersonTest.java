package jdk2010.util.function;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonTest {

	public static void main(String[] args) {
		Person a = new Person("zhangsan", 10);
		Person b = new Person("lisi",5);
		Person c = new Person("wangwu",15);
		List<Person> persons =new ArrayList<>();
		persons.add(a);
		persons.add(b);
		persons.add(c);
		
		PersonTest test = new PersonTest();
		List<Person> personsOut =test.getPersonByUserName("zhangsan", persons);
		
		personsOut.forEach(person -> System.out.println(person.getUsername()));
		
		
	}
	
	public List<Person> getPersonByUserName(String username,List<Person> persons){
		return persons.stream().filter(person -> person.getUsername().equals(username)).collect(Collectors.toList());
	}

}

package jdk2010.util.function;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public class ButtonTest {
	public static void main(String[] args) {
		Button btn = new Button("²âÊÔ");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("test");
			}
		});

		System.out.println("=========================");
		
		btn.addActionListener(event -> {
			System.out.println("test");
		});
		
		Runnable noArguments = () -> System.out.println("Hello World");
		new Thread(noArguments).start();
		
		BinaryOperator<Long> add =(x,y)-> x+y;
		
		System.out.println(add.apply(5L,6L));
		
		List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		
		features.forEach(n -> System.out.println(n));
		
		Map<String,Object> map=new HashMap<>();

		
		
	}
}

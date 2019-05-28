package jdk2010.lang.testenum;

public class TestWeather1Main {
	public static void main(String[] args) {
		
		for(Weather1 w:Weather1.values()){
			System.out.println(w);
		}
		System.out.println(Weather1.Cloudy.ordinal());
	}
}

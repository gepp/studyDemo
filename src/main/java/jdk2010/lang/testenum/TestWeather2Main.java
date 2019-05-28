package jdk2010.lang.testenum;

public class TestWeather2Main {
	public static void main(String[] args) {
		
		for(Weather2 w:Weather2.values()){
			System.out.println(w);
		}
		System.out.println(Weather2.Cloudy.ordinal());
		System.out.println(Weather2.Cloudy.getValue());
		System.out.println(Weather2.Cloudy.getLabel());
	}
}

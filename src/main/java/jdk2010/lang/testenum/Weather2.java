package jdk2010.lang.testenum;

public enum Weather2 {

	Sunny(1, "ÇçÌì"), Rainy(2, "ÓêÌì"), Cloudy(3, "¶àÔÆ");
	
	private int value;

	private String label;

	private Weather2(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
	
	
}

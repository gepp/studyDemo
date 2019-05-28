package jdk2010.lang.testenum;

public enum Weather2 {

	Sunny(1, "����"), Rainy(2, "����"), Cloudy(3, "����");
	
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

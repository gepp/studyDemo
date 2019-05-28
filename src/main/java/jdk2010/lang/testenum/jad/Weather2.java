package jdk2010.lang.testenum.jad;

public final class Weather2 extends Enum
{

	public static final Weather2 Sunny;
	public static final Weather2 Rainy;
	public static final Weather2 Cloudy;
	private int value;
	private String label;
	private static final Weather2 ENUM$VALUES[];

	private Weather2(String s, int i, int value, String label)
	{
		super(s, i);
		this.value = value;
		this.label = label;
	}

	public int getValue()
	{
		return value;
	}

	public String getLabel()
	{
		return label;
	}

	public static Weather2[] values()
	{
		Weather2 aweather2[];
		int i;
		Weather2 aweather2_1[];
		System.arraycopy(aweather2 = ENUM$VALUES, 0, aweather2_1 = new Weather2[i = aweather2.length], 0, i);
		return aweather2_1;
	}

	public static Weather2 valueOf(String s)
	{
		return (Weather2)Enum.valueOf(jdk2010/lang/testenum/Weather2, s);
	}

	static 
	{
		Sunny = new Weather2("Sunny", 0, 1, "«ÁÃÏ");
		Rainy = new Weather2("Rainy", 1, 2, "”ÍÃÏ");
		Cloudy = new Weather2("Cloudy", 2, 3, "∂‡‘∆");
		ENUM$VALUES = (new Weather2[] {
			Sunny, Rainy, Cloudy
		});
	}
}

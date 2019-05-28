package jdk2010.lang.testenum.jad;

public final class Weather1 extends Enum
{

	public static final Weather1 Sunny;
	public static final Weather1 Rainy;
	public static final Weather1 Cloudy;
	private static final Weather1 ENUM$VALUES[];

	private Weather1(String s, int i)
	{
		super(s, i);
	}

	public static Weather1[] values()
	{
		Weather1 aweather1[];
		int i;
		Weather1 aweather1_1[];
		System.arraycopy(aweather1 = ENUM$VALUES, 0, aweather1_1 = new Weather1[i = aweather1.length], 0, i);
		return aweather1_1;
	}

	public static Weather1 valueOf(String s)
	{
		return (Weather1)Enum.valueOf(jdk2010/lang/testenum/Weather1, s);
	}

	static 
	{
		Sunny = new Weather1("Sunny", 0);
		Rainy = new Weather1("Rainy", 1);
		Cloudy = new Weather1("Cloudy", 2);
		ENUM$VALUES = (new Weather1[] {
			Sunny, Rainy, Cloudy
		});
	}
}
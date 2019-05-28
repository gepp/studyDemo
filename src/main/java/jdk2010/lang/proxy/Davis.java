package jdk2010.lang.proxy;

/**
 * 球员安东尼戴维斯，实现了签约的方法，需要本人亲自签约
 */
public class Davis implements Talk {
	@Override
	public void sign() {
		System.out.println("1签约了，5年2.25亿美元");
	}
}

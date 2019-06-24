package geym.zbase.ch2.perm;

import java.util.HashMap;

/**
 * JDK1.6 1.7 -XX:+PrintGCDetails -XX:PermSize=5M -XX:MaxPermSize=5m
 * 
 * JDK1.8 -XX:+PrintGCDetails -XX:MaxMetaspaceSize=5M
 * 
 * @author Geym
 *
 */
public class PermTest {
	public static void main(String[] args) {
		int i = 0;
		try {
			for (i = 0; i < 100000; i++) {
				CglibBean bean = new CglibBean("geym.zbase.ch2.perm" + i, new HashMap());
			}
		} catch (Exception e) {
			System.out.println("total create count:" + i);
			throw e;
		}
		// 为了方便查看堆内存中对象个数，线程sleep
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

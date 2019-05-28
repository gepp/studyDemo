package jdk2010.lang.proxy.jdk;

import jdk2010.lang.proxy.Davis;
import jdk2010.lang.proxy.Talk;

public class TestProxy {
	public static void main(String[] args) {
		Talk talk1 = new Davis();

		JdkDynamicProxy handler = new JdkDynamicProxy(talk1);
		// ģʽ1
		// Talk proxyTalk = (Talk)
		// Proxy.newProxyInstance(talk.getClass().getClassLoader(),
		// talk.getClass().getInterfaces(), handler);
		// ģʽ2
		Talk proxyTalk = handler.getProxy();
		proxyTalk.sign();

		// Talk talk2 = new Davis2();
		// JdkDynamicProxy handler2 = new JdkDynamicProxy(talk2);
		// Talk proxyTalk2 = handler2.getProxy();
		// proxyTalk2.sign();

		// System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles",
		// "true");
		// Class proxyClass = Proxy.getProxyClass(
		// TestProxy.class.getClassLoader(),
		// new Class[] { Talk.class });
		//
		// Field[] fields=proxyClass.getDeclaredFields();
		// for(Field field:fields){
		// System.out.println(field.getName());
		// }
		//
		// Method[] methods=proxyClass.getMethods();
		// for(Method method:methods){
		// System.out.println(method.getName());
		// }

	}
}

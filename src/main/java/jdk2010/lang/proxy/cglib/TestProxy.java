package jdk2010.lang.proxy.cglib;

import jdk2010.lang.proxy.Davis;
import jdk2010.lang.proxy.Talk;

public class TestProxy {
	public static void main(String[] args) {
		CGLibProxy cgLibProxy = new CGLibProxy();
		Talk proxyTalk = cgLibProxy.getProxy(Davis.class);
		proxyTalk.sign();

	}
}

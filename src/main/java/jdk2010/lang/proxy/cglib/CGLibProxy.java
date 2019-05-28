package jdk2010.lang.proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CGLibProxy implements MethodInterceptor {

    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("����ӵ�����һ�����Աѡ��");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("ǩԼ�ɹ������׷��Ȩ��ʼ��Ч");
        return result;
    }

}
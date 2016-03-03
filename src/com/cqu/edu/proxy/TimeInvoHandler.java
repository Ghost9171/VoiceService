package com.cqu.edu.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TimeInvoHandler implements InvocationHandler {

	private Object obj;
	private long startTime;
	private long endTime;
	/*
	 * BusinessImplProxy() { System.out.println("test"); }
	 */
	TimeInvoHandler(Object obj) {
		this.obj = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		doBefore();
		result = method.invoke(obj, args);
		doAfter(method.getName());
		return result;
	}

	private void doAfter(String method) {
		// ������ʱ
		endTime = System.currentTimeMillis();
		System.out.println("��ʱΪ��" + (endTime - startTime) + "����");
	}

	private void doBefore() {
		// ��ʼ��ʱ
		startTime = System.currentTimeMillis();// ��ǰʱ���Ӧ�ĺ�����
	}
	
	public static Object factory(Object obj)
    {
        Class cls = obj.getClass();
        return Proxy.newProxyInstance(cls.getClassLoader(),cls.getInterfaces(),new TimeInvoHandler(obj));
    }

}

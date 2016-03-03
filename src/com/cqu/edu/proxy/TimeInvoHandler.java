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
		// 结束计时
		endTime = System.currentTimeMillis();
		System.out.println("耗时为：" + (endTime - startTime) + "毫秒");
	}

	private void doBefore() {
		// 开始计时
		startTime = System.currentTimeMillis();// 当前时间对应的毫秒数
	}
	
	public static Object factory(Object obj)
    {
        Class cls = obj.getClass();
        return Proxy.newProxyInstance(cls.getClassLoader(),cls.getInterfaces(),new TimeInvoHandler(obj));
    }

}

package com.zt.thread.reference;

public class M {
	
	/**
	 * 重写finalize会导致OOM问题？OOM改怎么定位？这个是产生OOM的原因之一
	 * 
	 * finalize掉用时机：此对象被垃圾回收器回收时，finalize会被调用
	 */
	@Override
	protected void finalize() throws Throwable {
		System.out.println("finalize");
	}

}

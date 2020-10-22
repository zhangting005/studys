package com.zt.thread.reference;

import java.lang.ref.WeakReference;

/**
 * 弱引用：只要调用了垃圾回收（System.gc()）就回收
 * 应用场景：只要强引用消失，则应该被回收，一般用在容器里，典型应用ThreadLock，看下WeakHashMap、AQSunlock源码（Tomcat缓存用的是弱应用）
 */
public class T03_WeakReference {
	
	public static void main(String[] args) {
		
		WeakReference<M> m = new WeakReference<>(new M());
		
		System.out.println(m.get());
		System.gc();
		System.out.println(m.get());
		
		//ThreadLocal对弱引用有典型的应用，面试经常被用到
		ThreadLocal<M> tl = new ThreadLocal<>();
		tl.set(new M());
		tl.remove();// 必须ThreadLocal用完必须remove，否则还是有内存泄漏
	}
}

package com.zt.jvm;

import org.openjdk.jol.info.ClassLayout;
/**
 * JOL:JAVA OBJECT LAYOUT
 * @author lenovo
 *
 */
public class HelloJOL {

	public static void main(String[] args) {
		Object o = new Object();
		//未加锁的内存布局
		String s = ClassLayout.parseInstance(o).toPrintable();
		System.out.println(s);
		
		//hashcode是0？因为只有调用了对象的hashcode，值才会发生改变
		o.hashCode();
		//加锁的内存布局
		synchronized (o) {
			System.out.println(ClassLayout.parseInstance(o).toPrintable());
		}
	}

}

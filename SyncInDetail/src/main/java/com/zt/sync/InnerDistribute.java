package com.zt.sync;

import org.openjdk.jol.info.ClassLayout;

public class InnerDistribute {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Object o = new Object(); 
		System.out.println(ClassLayout.parseInstance(o).toPrintable());
		
		System.out.println("----------------------------");
		o.hashCode();
		System.out.println(ClassLayout.parseInstance(o).toPrintable());
		
//		User user = new User();
//		System.out.println(ClassLayout.parseInstance(user).toPrintable());
		System.out.println("----------------------------"); 
		synchronized(o) {
			System.out.println(ClassLayout.parseInstance(o).toPrintable());

		}
	}
}

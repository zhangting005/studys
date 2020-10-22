package com.zt.thread.reference;

import java.util.concurrent.TimeUnit;

public class ThreadLocal2 {

	static ThreadLocal<Person> tl = new ThreadLocal<>();

	public static void main(String args[]) {
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(tl.get());
		}).start();

		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tl.set(new Person());
		}).start();
	}

	static class Person {
		String name = "xxx";
	}

}

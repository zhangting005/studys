package com.zt.sync.singlepattern;

/**
 * 代码示例3：懒汉式 说明：为了解决不管是否使用都会实例化的问题，但是却带来线程不安全的问题
 */
public class Mgr03 {
	public static void main(String[] args) {
		//在多线程下，new出来的对象肯定不止一个，可以查看结果，这些对象的hashcode会不相同，证明了不是一个对象，线程不安全
		for (int i = 0; i < 50; i++) {
			new Thread(() -> {
				System.out.println(Mgr03.getInstance().hashCode());
			}).start();
		}
	}

	// 首先定义一个instance，不把它new出来
	private static Mgr03 INSTANCE;

	// 构造方法私有化
	private Mgr03() {
	};

	public static Mgr03 getInstance() {
		// 什么时候有人调用instance方法，就什么时候new出来
		// 先判断下，如果没有new出来，我就直接new出来，如果已经new出来，直接return就行
		// 多线程下，线程不安全举例：当第一个线程进来，判断为空，准备new一个实例，在没有new完时，第二个线程进来，还是判断为空，又new出来一个，这两个不是同一个对象
		if (INSTANCE == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			INSTANCE = new Mgr03();
		}
		return INSTANCE;
	}

	public void m() {
		System.out.println("Mgr01");
	}
}

package com.zt.sync.singlepattern;

/**
 * 代码示例4：懒汉式+锁
 * 说明： 通过给getInstance()方法加锁，解决对象不唯一的问题，但是同时也带来效率下降的问题
 */
public class Mgr04 {
	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			new Thread(() -> {
				System.out.println(Mgr03.getInstance().hashCode());
			}).start();
		}
	}

	private static Mgr04 INSTANCE;

	// 构造方法私有化
	private Mgr04(){};

	public static synchronized Mgr04 getInstance() {
		if (INSTANCE == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			INSTANCE = new Mgr04();
		}
		return INSTANCE;
	}

	public void m() {
		System.out.println("Mgr01");
	}
}

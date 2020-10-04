package com.zt.sync.singlepattern;

/**
 * 代码示例5：懒汉式+锁+减少同步代码块
 * 说明：同样存在线程不安全的问题，因为第一个if判断条件有可能导致在对象没有实例化之前，不同的线程进入到if代码块中，拿到锁之后进行实例化
 */
public class Mgr05 {
	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			new Thread(() -> {
				System.out.println(Mgr05.getInstance().hashCode());
			}).start();
		}
	}

	private static Mgr05 INSTANCE;

	private Mgr05() {
	};

	public static Mgr05 getInstance() {
		//业务代码写在这里
		if (INSTANCE == null) {
			// 试图通过减小同步代码块的方式提高效率，但是并不可行，线程不安全
			// 线程不安全举例：当第一个线程进来，准备上锁但是还没有上锁的时候，第二个线程又来了，此时判断还是空，第二个线程上锁，锁住了后new了对象拿走了
			               // 第一个线程，由于第二个线程已经释放了锁，第一个线程会重新new一个新对象，导致线程不安全
			//最终导致了方法6：美团问题
			synchronized (Mgr05.class) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				INSTANCE = new Mgr05();
			}
		}
		return INSTANCE;
	}

	public void m() {
		System.out.println("Mgr01");
	}

}

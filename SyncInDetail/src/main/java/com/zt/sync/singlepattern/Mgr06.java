package com.zt.sync.singlepattern;

/**
 * 代码示例6：懒汉式+锁+双重检查 DCL 可以保证线程安全
 */
public class Mgr06 {
	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			new Thread(() -> {
				System.out.println(Mgr06.getInstance().hashCode());
			}).start();
		}
	}

	//DCL单例到底需不需要volatile
	//需要，举例说明：线程1先判断单例对象是否为空，为空，new，new到一半时，发生了指令重排序，还没来得及执行构造方法，
	//就被线程2，判断单例对象不为空，直接将对象拿去用，这个对象是半初始化的，举例来说，如果拿到一个成员变量，线程1赋值为10000，因为半初始化状态，线程2只能拿到默认值0
	private static volatile Mgr06 INSTANCE;

	private Mgr06() {
	};

	public static Mgr06 getInstance() {
		// 业务逻辑代码省略
		if (INSTANCE == null) {// Double Check Lock
			// 双重检查
			synchronized (Mgr06.class) {
				if (INSTANCE == null) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					INSTANCE = new Mgr06();
				}
			}
		}
		return INSTANCE;
	}

	public void m() {
		System.out.println("Mgr01");
	}
}

/**
 * 对象实例化实际可以分为几个步骤：
   1、分配对象空间
   2、初始化对象
   3、将对象指向分配的内存空间
          当指令重排的时候，2 和 3 会进行重排序，导致有的线程可能拿到未初始化的对象调用，存在风险问题。
*/

/**
    什么是指令重排？
    指令重排序是JVM为了优化指令，提高程序运行效率。指令重排序包括编译器重排序和运行时重排序。JVM规范规定，指令重排序可以在不影响单线程程序执行结果前提下进行。
    既然这样，那么在应用真正运行时可能是这个样子的：
    1.分配内存空间
    2.将内存空间的地址赋值给对应的引用
    3.初始化对象
*/
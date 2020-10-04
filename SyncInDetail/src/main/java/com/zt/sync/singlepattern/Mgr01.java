package com.zt.sync.singlepattern;

/**
 * 代码示例1：饿汉式
 */
public class Mgr01 {
	public static void main(String[] args) {
		//不论调用多少次getInstance方法，永远拿到的是一个对象
		Mgr01 m1 = Mgr01.getInstance();
		Mgr01 m2 = Mgr01.getInstance();
		System.out.println(m1 == m2);
	}

   //1.class文件加载时进行类的实例化，new出来
	private static Mgr01 INSTANCE = new Mgr01();

   //构造方法私有化，不允许别人new
	private Mgr01() {
	};

	//别人不能new了，怎么使用这个对象？给这个类加一个方法，让拿到这个instance，将instance return回去
	public static Mgr01 getInstance() {
		return INSTANCE;
	}

	public void m() {
		System.out.println("Mgr01");
	}
}
/**
 * 说明：
 * 以上是饿汉式单例，就是在类加载的时候完成实例化，而且只有一个实例；同时JVM能够保证线程安全，简单实用，推荐使用。
 * 缺点：不管是否用到，类装载的时候就已经完成了实例化
*/

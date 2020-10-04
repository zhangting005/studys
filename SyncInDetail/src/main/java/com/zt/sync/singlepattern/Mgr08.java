package com.zt.sync.singlepattern;

/**
 * 代码示例8：枚举类 
 * 说明：以上方法不单能实现单例，而且能防止被反序列化，因为枚举类没有构造方法，所以即使得到了class文件也不能被反序列化。
 */
public enum Mgr08 {
	INSTANCE;
	public void m() {
		System.out.println("Mgr08");
	}

	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			new Thread(() -> {
				System.out.println(Mgr08.INSTANCE.hashCode());
			}).start();
		}
	}
}

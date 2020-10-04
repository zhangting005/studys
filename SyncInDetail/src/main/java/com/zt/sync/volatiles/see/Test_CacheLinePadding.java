package com.zt.sync.volatiles.see;

/**
 * @功能说明： 验证多线程在修改统一缓存行和修改不同缓存行效率问题 结论：
 * 两个线程在同时修改两个数据时，如果这两个数据在同一个缓存块中，效率要低于两个数据在不同缓存块的情况，
 * 因为同一缓存中的数据在被更新的时候会通过缓存锁等待相互更新完毕后再同步数据， 而不同缓存中不需要通知其它缓存行数据已经过期，所以速度快。
 * 
 * concurrent并发包里面很多也是这种写法，利用缓存对齐填满64个字节，是的数据在不同缓存块中，减少线程间数据同步的消耗。
 */
public class Test_CacheLinePadding {
	public static class Padding {
		// 一个long 8个字节 56个字节
		public volatile long x1, x2, x3, x4, x5, x6, x7;
	}

	// 继承一个 56 字节的 Padding + T 中的 x 组成64个字节，
	// 构建要修改的数据分别在不同缓存行的情况
	private static class T extends Padding {
		public volatile long x = 0L;
	}

	public static T[] arr = new T[2];
	static {
		arr[0] = new T();
		arr[1] = new T();
	}

	public static void test2() throws Exception {
		Thread t1 = new Thread(() -> {
			for (long i = 0; i < 10000000L; i++) {
				arr[0].x = i;
			}
		});
		Thread t2 = new Thread(() -> {
			for (long i = 0; i < 10000000L; i++) {
				arr[1].x = i;
			}
		});
		final long start = System.nanoTime();
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		final long end = System.nanoTime();
		System.out.println((end - start) / 1000000);
	}

	public static void main(String[] args) throws Exception {
		// 以下数据为多次执行的结果 ms
		// test2(); // 210 269 216 232 不继承 Padding 的情况 修改数据在相同Cache line
		test2(); // 112 112 104 143 继承 Padding 的情况 修改数据在不同Cache line
	}
}

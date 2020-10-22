package com.zt.thread.reference;

/**
 * 软引用
 * 软引用是用来描述一些还有用但并非必须的对象。
 * 对于软引用关联着的对象，在系统将要发生内存溢出异常之前，将会把这些对象列进回收范围进行第二次回收。
 * 如果这次回收还没有足够的内存，才会抛出内存溢出异常。
 * -Xmx20M -Xms20M
 */

/**
 * 软引用（Soft Reference）：空间不够才回收，用于缓存
　  如果一个对象只具有软引用，在内存足够时，垃圾回收器不会回收它；如果内存不足，就会回收这个对象的内存。
　  使用场景:图片缓存。图片缓存框架中，“内存缓存”中的图片是以这种引用保存，使得 JVM 在发生 OOM 之前，可以回收这部分缓存。
     一般是垃圾回收器实现
 */
import java.lang.ref.SoftReference;

public class T02_SoftReference {
	public static void main(String[] args) {
		SoftReference<byte[]> m = new SoftReference<>(new byte[1024 * 1024 * 10]);
		// m = null;
		System.out.println(m.get());
		System.gc();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(m.get());

		// 再分配一个数组，heap将装不下，这时候系统会垃圾回收，先回收一次，如果不够，会把软引用干掉
		byte[] b = new byte[1024 * 1024 * 15];
		System.out.println(m.get());
	}
}
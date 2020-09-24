package com.zt.first.three.nine;

public class CuLock {

	public static void main(String args[]) throws InterruptedException {
		Integer CIRCLE = 2000000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < CIRCLE; i++) {
			craeteStringBuffer("JVM", "Diagnosis");
		}
		long bufferCost = System.currentTimeMillis() - start;
		System.out.println("craeteStringBuffer: " + bufferCost + " ms");
	}

	public static String craeteStringBuffer(String s1, String s2) {
		StringBuffer sb = new StringBuffer();
		// StringBuffer是有同步的，是线程安全的（类似vector，内部有JVM实现锁）
		sb.append(s1);
		sb.append(s2);
		return sb.toString();
	}

}

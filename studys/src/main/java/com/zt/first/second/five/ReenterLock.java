package com.zt.first.second.five;

import java.util.concurrent.locks.ReentrantLock;

//重入锁
public class ReenterLock implements Runnable {

	//一般是非公平锁，公平锁的性能比非公平锁差，因为公平锁需要处理派对问题
	public static ReentrantLock Lock = new ReentrantLock();

	public static int i = 0;

	// synchronize是有虚拟机控制，会由jvm释放锁;但reentryLock是由程序控制在什么是假释放锁，提供了锁释放的灵活性，可以在任何场景下释放
	@Override
	public void run() {
		for (int j = 0; j < 1000000; j++) {
			Lock.lock();
			try {
				i++;
			} finally {
				Lock.unlock();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ReenterLock tl = new ReenterLock();
		Thread t1 = new Thread(tl);
		Thread t2 = new Thread(tl);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(i);
	}

}
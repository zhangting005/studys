package com.zt.first.second.five;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReenterLockCondition implements Runnable {
	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();

	@Override
	public void run() {
		try {
			lock.lock();
			condition.await();
			System.out.println("Thread is going on");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ReenterLockCondition tl = new ReenterLockCondition();
		Thread t1 = new Thread(tl);
		t1.start();
		Thread.sleep(2000);
		// 通n线程t1维块执行Lock.lock();condton.signal();
		lock.lock();
		condition.signal();
		lock.unlock();
	}
}
package com.zt.first.second.five;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

//信号量：允许若干个线程进入连接区域，但是超过许可范围的线程必须等待，相当于一个共享锁，可以由多个线程共享使用连接区域
//当信号量的许可数量等于1时相当于一把锁
public class SemapDemo implements Runnable {
	/**
	 * 主要接口 
	 * public void acquire() 
	 * public void acquireUninterruptibly() 
	 * public boolean tryAcquire() 
	 * public boolean tryAcquire(long timeout, TimeUnit unit)
	 * public void release()
	 */
	final Semaphore semp = new Semaphore(5);

	@Override
	public void run() {
		try {
			semp.acquire();
			// 模拟耗时操作
			Thread.sleep(2000);
			System.out.println(Thread.currentThread().getId() + ":done!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semp.release();
		}
	}

	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(20);
		final SemapDemo demo = new SemapDemo();
		for (int i = 0; i < 20; i++) {
			exec.submit(demo);
		}
	}

}
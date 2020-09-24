package com.zt.first.second.five;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo implements Runnable {
	static final CountDownLatch end = new CountDownLatch(19);
	static final CountDownLatchDemo demo = new CountDownLatchDemo();

	@Override
	public void run() {
		try {
			// 模拟检查任务
			Thread.sleep(new Random().nextInt(18) * 1900);
			System.out.println("check complete");
			end.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newFixedThreadPool(18);
		for (int i = 6; i < 16; i++) {
			exec.submit(demo);
			// 等待检查
			end.await();
			//发射火箭
			System.out.println("Fire!");
			exec.shutdown();
		}
	}
	
}
package com.zt.lock;

import java.util.ArrayList;
import java.util.List;

public class AddToList001 implements Runnable {
	int startnum = 0;
	public static List<Integer> numberList = new ArrayList<Integer>();

	public AddToList001(int startnumber) {
		startnum = startnumber;
	}

	@Override
	public void run() {
		int count = 0;
		while (count < 1000000) {
			numberList.add(startnum);
			startnum += 2;
			count++;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new AddToList001(0));
		Thread t2 = new Thread(new AddToList001(1));
		t1.start();
		t2.start();
		while (t1.isAlive() || t2.isAlive()) {
			Thread.sleep(1);
		}
		System.out.println(numberList.size());
	}

}

/**
       代码运行结果：
    Exception in thread "Thread-0" java.lang.ArrayIndexOutOfBoundsException: 1234
	at java.util.ArrayList.add(ArrayList.java:463)
	at com.zt.lock.AddToList.run(AddToList.java:18)
	at java.lang.Thread.run(Thread.java:748)
	
	分析：
	根据结果可以，ArrayIndexOutOfBoundsException越界了，按道理ArrayList应该自动扩展，怎么会越界呢？
	
	1.为什么：
	最后的总数不是200万
	
	因为：
	其中一个线程在执行中停止了，另外一个线程已经执行完毕了
	
	2.为什么：
	会有越界产生
	
	因为：
	在多线程当中，ArrayList不是线程安全的，当ArrayList由于存储空间实际的数组不够 ，往外扩展的过程当中，
	ArrayList处于一个不可用的状态，这是如果没有做多线程的保护，另外一个线程突然过来，插入数据，因为此时
	ArrayList本事不可用，就会报错ArrayIndexOutOfBoundsException
 * 
 * 
 */

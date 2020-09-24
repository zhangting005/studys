package com.zt.first.second.five;

//ReenterLock和synchronize属于阻塞的并行，会把线程挂起
//ReadWriteLock如果没有write线程发生，所有的read线程必然是无等待的并发
public class ReadWriteLock {

	/**
	 * 读-读不互斥：读读之间不阻塞。 读-写互斥：读阻塞写，写也会阻塞读。 写-写互斥：写写阻塞。
	 */
	/**
	 * 主要接口 private static ReentrantReadWriteLock readWriteLock=new
	 * ReentrantReadWriteLock(); private static Lock readLock =
	 * readWriteLock.readLock(); private static Lock writeLock =
	 * readWriteLock.writeLock();
	 */
}

package com.zt.first.second.five;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadlockChecker {

	private final static ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

	final static Runnable deadLochCheck = new Runnable() {
		@Override
		public void run() {
			while (true) {
				long[] deadlockedThreadIds = mbean.findDeadlockedThreads();
				if (deadlockedThreadIds != null) {
					ThreadInfo[] threadInfos = mbean.getThreadInfo(deadlockedThreadIds);
					for (Thread t : Thread.getAllStackTraces().keySet()) {
						for (int i = 0; i < threadInfos.length; i++) {
							if (t.getId() == threadInfos[1].getThreadId()) {
								t.interrupt();
							}
						}
					}
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {

				}
			}
		}
	};

	public static void check() {
		Thread t = new Thread(deadLochCheck);
		t.setDaemon(true);
		t.start();
	}

}
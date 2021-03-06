package com.zt.first.three.nine;

/**
 * 偏向锁
 */
import java.util.List;
import java.util.Vector;

public class BiasLock002 {
	//vector内部是有synchronize操作的，add时候会有锁操作的，虽然看起没有锁操作，实际上在JDK是有的
	public static List<Integer> numberList = new Vector<Integer>();

	public static void main(String[] args) throws InterruptedException {
		long begin = System.currentTimeMillis();
		int count = 0;
		int startnum = 0;
		while (count < 10000000) {
			numberList.add(startnum);//add时候是有锁操作的
			startnum += 2;
			count++;
		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin);

	}
}
//用下面两种方式去执行：性能有5%的差别
//第一种：使用偏向锁-因为系统在启动时，锁竞争往往是很激烈的，所以系统刚启动的前几秒可能不会开启偏向锁（索静正激烈，用偏向锁反而会损耗性能，详细见001），因为当前启动时间肯定很短，根本 用不了几秒钟，因此这里参数设置为0，可以让程序启动的当时就启用偏向锁
//-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
//执行结果 2896
//第二种：不适用偏向锁
//-XX:-UseBiasedLocking
//执行结果：3063


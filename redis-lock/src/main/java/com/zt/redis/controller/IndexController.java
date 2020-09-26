package com.zt.redis.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis分布式锁递进优化：deductStock - > deductStock4
 * @author lenovo
 *
 */
@RestController
public class IndexController {

	@Autowired
	private Redisson redisson;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@RequestMapping("/deduct_stock")
	public String deductStock() {
		int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));// 假设给初始值50
		/**
		 * 很明显，代码是有问题的：线程安全并发访问的问题 假设有3个用户同时访问这个接口，拿到的库存都是50，大家都减去1，结果得到的结果都是49，最终结果为49
		 * 但是最终错误肯定是47 那么这个代码肯定需要完善下
		 */
		int reakStock = 0;
		if (stock > 0) {
			reakStock = stock - 1;
			stringRedisTemplate.opsForValue().set("stock", reakStock + "");
			System.out.println("扣减成功，剩余库存：" + reakStock + "");
		} else {
			System.out.println("扣减成功失败，库存不足");
		}
		return "end" + String.valueOf(reakStock);
	}

	@RequestMapping("/deduct_stock1")
	public String deductStock1() {
		/**
		 * 在上面的基础上加synchronized保证线程安全 如果公司是单体架构（就一个Tomcat去运行，跑在线上环境），那么这个代码应该没有问题，
		 * 性能问题等会儿再说 但如果是集群架构，这个代码会有问题
		 * 因为synchronized是JVM进程级别锁，对于多个Tomcat，每个Tomcat都是一个jvm进程，所以对于集群来说会有并发问题
		 */
		int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));// 假设给初始值50
		synchronized (this) {
			if (stock > 0) {
				int reakStock = stock - 1;
				stringRedisTemplate.opsForValue().set("stock", reakStock + "");
				System.out.println("扣减成功，剩余库存：" + reakStock + "");
			} else {
				System.out.println("扣减成功失败，库存不足");
			}
		}
		return "end";
	}

	@RequestMapping("/deduct_stock2")
	public String deductStock2() {
		/**
		 * 使用redis分布式锁实现集群下高并发问题解决方案 这种很简单就实现了分布式锁，但是存在很多问题
		 * 1.如果执行代码出现了异常，就无法释放锁：加try...finally
		 *
		 * 另外：这把锁相当于不在JVM进程，而在于redis
		 */
		// 为了方便理解，假设现在在抢购一件商品，商品id是：product_001
		// 相当于大家对同一个商品id在redis那边上把锁，即放一个key
		String lockKey = "product_001";
		try {
			// 返回false，说明这个key已经存在，redis不会再做任何操作
			// 1、Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("lockKey",
			// "zhuge");//jedis.setnx(key,value)
			// 如果项目正在发布，代码正好执行到finally之前，finally里面的锁就永远不会释放，这个怎么解决：利用expire设置失效时间
			// 2、stringRedisTemplate.expire(lockKey,10, TimeUnit.SECONDS);

			// 上面的1和2是有bug的，因为可能正好代码执行完1，就出现了异常，那么这个锁还是没有释放
			Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "zhuge", 10, TimeUnit.SECONDS);
			if (!result) {
				// 真实肯定比较友好，返回个提示，如：当前抢购人数过多，请稍后再试
				return "error";
			}
			int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));// jedis.get("stock")//假设给初始值50
			synchronized (this) {
				if (stock > 0) {
					int reakStock = stock - 1;
					stringRedisTemplate.opsForValue().set("stock", reakStock + "");// jedis.set(key,value)
					System.out.println("扣减成功，剩余库存：" + reakStock + "");
				} else {
					System.out.println("扣减成功失败，库存不足");
				}
			}
		} finally {
			// 释放锁：直接将这把锁的key删掉即可
			stringRedisTemplate.delete(lockKey);
		}

		return "end";
		/*
		 * 总结：经过上述优化，这个方法还有没有问题呢？
		 * 有可能前一个请求时间较长，expire过期时间为10秒，请求总耗时为15秒，那么在请求执行到一半，锁就因redis过期而失效了。
		 * 第二个线程马上拿到这个锁，但是当第一个线程执行完成，会释放锁，此时释放的锁是第一个线程的锁，类似这种恶性循环
		 * 出现线程刚加锁，就被其他线程释放掉了，有可能导致锁的永久失效
		 */
	}

	@RequestMapping("/deduct_stock3")
	public String deductStock3() {
		/**
		 * 怎么解决以上问题(高并发下锁永久失效的问题)呢？ 只要给每个线程加一个唯一标识 如：clientId
		 */
		String lockKey = "product_001";

		// 每个线程加一个唯一标识 如：clientId
		String clientId = UUID.randomUUID().toString();

		try {
			// 将clientId替redis中的value值,加锁时将key对应的value值设为线程的唯一标识
			// Boolean result
			// =stringRedisTemplate.opsForValue().setIfAbsent(lockKey,"zhuge",10,TimeUnit.SECONDS);
			Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
			if (!result) {
				return "error";
			}
			int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
			synchronized (this) {
				if (stock > 0) {
					int reakStock = stock - 1;
					stringRedisTemplate.opsForValue().set("stock", reakStock + "");
					System.out.println("扣减成功，剩余库存：" + reakStock + "");
				} else {
					System.out.println("扣减成功失败，库存不足");
				}
			}
		} finally {
			// 释放锁时先判断下，当前线程的id和拿到的锁的value值是不是一样的
			if (clientId.contentEquals(stringRedisTemplate.opsForValue().get(lockKey))) {
				stringRedisTemplate.delete(lockKey);
 			}
		}

		return "end";
	}
	
	/*
	 * 这种写法已经在一线互联网公司使用很多年了，很成熟，可以放心使用
	 * 锁续命机制
	 */
	@RequestMapping("/deduct_stock4")
	public String deductStock4() {
		/**
		 * 失效时间 < 任务执行时间 导致锁失效问题
		 * 怎么解决失效时间问题？
		 * 使用已经存在的框架：Redisson
		 * 很多之前的代码就可以删掉了
		 */
		String lockKey = "product_001";

		String clientId = UUID.randomUUID().toString();

		//先拿到这把锁对象
		RLock redissionLock = redisson.getLock(lockKey);
		
		try {
			
//			Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
//			if (!result) {
//				return "error";
//			}
			
			//此处底层帮助我们往redis加一把锁，如果有多个线程同时请求，拿到锁的正常往下执行，其他没拿到锁的可能线程阻塞在此处（也存在API让不阻塞 ，直接返回）
			//多个请求到此的，只有第一个能够顺利执行完这一行代码，然后继续执行后面的，其他的线程阻塞在此
			//redis锁续命周期设置为设置时间的三分之一
			redissionLock.lock(30,TimeUnit.SECONDS);
			
			int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
			
			synchronized (this) {
				if (stock > 0) {
					int reakStock = stock - 1;
					stringRedisTemplate.opsForValue().set("stock", reakStock + "");
					System.out.println("扣减成功，剩余库存：" + reakStock + "");
				} else {
					System.out.println("扣减成功失败，库存不足");
				}
			}
		} finally {
			redissionLock.unlock();
//			if (clientId.contentEquals(stringRedisTemplate.opsForValue().get(lockKey))) {
//				stringRedisTemplate.delete(lockKey);
// 			}
		}

		return "end";
	}
	/**
	 * 在redis主从架构中，可能出现：第一个线程加锁成功，在redis的主节点有锁对应的key值lockKey
	 * redis主从架构意味着，锁在redis主节点设置成功后，要将锁的key值往redis的从节点同步
	 * 假设：从节点没有来的及同步主节点的锁，而主节点已经告诉客户端加锁成功，可以执行加锁的业务代码逻辑了
	 *       但是在主节点同步锁信息到从节点之前，redis主节点挂掉了，redis会选举，从剩下的从节点中选举主节点，
	 *       1.假设从节点变成新的主节点，其他的线程访问redis，此时肯定访问新的主节点，新的主节点因同步lockKey失败，是没有lockKey的，如何解决？
	 *       2.众所周知，redis的并发量已经很高了，达到每秒几万的并发，但如果我现在需要将并发量提升10倍，100倍呢，应该怎么解决？
	 *       
	 *       2答：假设产品 product_001 的库存是100，减库存时redis放key值为product_001
	 *            如果想把性能提升10倍，可以把库存在redis做分段存储
	 *            如：product_001的(第一个库存段是 product_001_1,库存10),...,(第十个库存段是 product_001_10,库存10)：即在redis中key是product_001_1，value是10；
	 *                类似这种做10个分段.
	 *                减库存的时候分段减，不同的高并发请求，锁定的key是不同的key
	 *                
	 *                如果想提升20倍，同理，可以将库存分20个分段，每个分段库存设置为5
	 *                
	 */

}
package com.zt.redis.controller;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private Redisson redission;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/deduct_stock")
    public String deductStock() {
        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));//假设给初始值50
        /**
         * 很明显，代码是有问题的：线程安全并发访问的问题
         * 假设有3个用户同时访问这个接口，拿到的库存都是50，大家都减去1，结果得到的结果都是49，最终结果为49
         * 但是最终错误肯定是47
         * 那么这个代码肯定需要完善下
         */
        int reakStock = 0;
        if (stock > 0) {
            reakStock = stock - 1;
            stringRedisTemplate.opsForValue().set("stock", reakStock + "");
            System.out.println("扣减成功，剩余库存：" + reakStock + "");
        } else {
            System.out.println("扣减成功失败，库存不足");
        }
        return "end"+ String.valueOf(reakStock);
    }

    @RequestMapping("/deduct_stock1")
    public String deductStock1() {
        /**
         * 在上面的基础上加synchronized保证线程安全
         * 如果公司是单体架构（就一个Tomcat去运行，跑在线上环境），那么这个代码应该没有问题，
         * 性能问题等会儿再说
         * 但如果是集群架构，这个代码会有问题
         * 因为synchronized是JVM进程级别锁，对于多个Tomcat，每个Tomcat都是一个jvm进程，所以对于集群来说会有并发问题
         */
        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));//假设给初始值50
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
         * 使用redis分布式锁实现集群下高并发问题解决方案
         * 这种很简单就实现了分布式锁，但是存在很多问题
         * 1.如果执行代码出现了异常，就无法释放锁：加try...finally
         *
         * 另外：这把锁相当于不在JVM进程，而在于redis
         */
        //为了方便理解，假设现在在抢购一件商品，商品id是：product_001
        //相当于大家对同一个商品id在redis那边上把锁，即放一个key
        String lockKey = "product_001";
        try {
            //返回false，说明这个key已经存在，redis不会再做任何操作
            //1、Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("lockKey", "zhuge");//jedis.setnx(key,value)
            //如果项目正在发布，代码正好执行到finally之前，finally里面的锁就永远不会释放，这个怎么解决：利用expire设置失效时间
            //2、stringRedisTemplate.expire(lockKey,10, TimeUnit.SECONDS);

            //上面的1和2是有bug的，因为可能正好代码执行完1，就出现了异常，那么这个锁还是没有释放
            Boolean result =stringRedisTemplate.opsForValue().setIfAbsent(lockKey,"zhuge",10,TimeUnit.SECONDS);
            if (!result) {
                //真实肯定比较友好，返回个提示，如：当前抢购人数过多，请稍后再试
                return "error";
            }
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));//jedis.get("stock")//假设给初始值50
            synchronized (this) {
                if (stock > 0) {
                    int reakStock = stock - 1;
                    stringRedisTemplate.opsForValue().set("stock", reakStock + "");//jedis.set(key,value)
                    System.out.println("扣减成功，剩余库存：" + reakStock + "");
                } else {
                    System.out.println("扣减成功失败，库存不足");
                }
            }
        }finally {
            //释放锁：直接将这把锁的key删掉即可
            stringRedisTemplate.delete(lockKey);
        }


        return "end";
    }

}
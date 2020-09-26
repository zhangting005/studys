package com.zt.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RedisLockApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisLockApplication.class, args);
	}
	
	//初始化redis客户端
	@Bean
    Redisson redissonSingle() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.153.129:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }

}

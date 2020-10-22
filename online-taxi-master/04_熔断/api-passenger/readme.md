#ribbon远程调用
第一步:引入jar包，这一步不需要做，因为在eureka中包含了

第二步:加入客户端的负载均衡@LoadBalanced，api-passenger中SmsController相当于服务消费者;service-sms相当于服务的提供者

    @EnableEurekaClient
	 @SpringBootApplication
	 @EnableFeignClients
	 @EnableCircuitBreaker
	 @ComponentScan(excludeFilters = {
			@ComponentScan.Filter(type = 	FilterType.ANNOTATION,value=ExcudeFeignConfig.class)
	})
	public class ApiPassengerApplication {

		public static void main(String[] args) {
			SpringApplication.run(ApiPassengerApplication.class, args);
		}
	
	
		@Bean
		@LoadBalanced
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	
	}
	
第三步：引入RestTemplate

    @Autowired
	 private RestTemplate restTemplate;
	
	 //ribbon调用
	 ResponseEntity<ResponseResult> resultEntity = restTemplate.postForEntity(url, smsSendRequest, ResponseResult.class);
	 ResponseResult result = resultEntity.getBody();
	 
	 
说明：此项目为api-passenger调用service-sms的短信服务，数据库连接schema为online-taxi
1.数据库表sql
DROP TABLE IF EXISTS `tbl_sms`;
CREATE TABLE `tbl_sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(16) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '乘客手机号',
  `sms_content` varchar(512) NOT NULL DEFAULT '' COMMENT '短信内容',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `operator` varchar(255) NOT NULL DEFAULT '' COMMENT '操作人',
  `send_flag` tinyint(1) NOT NULL COMMENT '发送状态 0:失败  1: 成功',
  `send_number` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发送失败次数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1973 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tbl_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sms_template`;
CREATE TABLE `tbl_sms_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `template_id` varchar(16) NOT NULL DEFAULT '' COMMENT '短信模板ID',
  `template_name` varchar(128) DEFAULT NULL,
  `content` varchar(512) NOT NULL DEFAULT '' COMMENT '模板内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `template_type` tinyint(1) NOT NULL DEFAULT '3' COMMENT '模板类型（1：营销；2：通知；3：订单）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `TEMPLATE_ID` (`template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;

2.需要插入短信模板数据
INSERT INTO `tbl_sms_template` VALUES ('5', 'SMS_144145499', '登录验证码', '手机验证码：${code}，10分钟内有效。如非本人操作，请忽略。', '2018-09-07 15:00:11', '2018-09-12 13:24:14', '3');

3.postman接口描述
调用接口:127.0.0.1:9001/sms/verify-code/send
格式:raw json
传参:{"phoneNumber":"15971398300"}
查验成功结果：tbl_sms多了数据



==============================================

#ribbon换成feign，feign的使用
第一步：调用方api-passenger的pom加依赖
        <!-- 引入feign依赖 ，用来实现接口伪装 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>
		
第二步：启动类添加注解@EnableFeignClients

    @EnableEurekaClient
	 @SpringBootApplication
	 @EnableFeignClients
	 @EnableCircuitBreaker
	 @ComponentScan(excludeFilters = {
			@ComponentScan.Filter(type = 	FilterType.ANNOTATION,value=ExcudeFeignConfig.class)
	})
	public class ApiPassengerApplication {

		public static void main(String[] args) {
			SpringApplication.run(ApiPassengerApplication.class, args);
		}
	
	
		@Bean
		@LoadBalanced
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	
	}
	
第三步：定义接口

	//@FeignClient(name = "service-sms",configuration = FeignDisableHystrixConfiguration.class)
	//@FeignClient(name = "service-sms",fallbackFactory = SmsClientFallbackFactory.class)
	@FeignClient(name = "service-sms",fallback = SmsClientFallback.class)
	//@FeignClient(name = "service-sms")
	public interface SmsClient {
		/**
	 	* 按照短信模板发送验证码
	 	* @param smsSendRequest
	 	* @return
	 	*/
		@RequestMapping(value="/send/alisms-template", method = RequestMethod.POST)
		public ResponseResult sendSms(@RequestBody SmsSendRequest smsSendRequest);
	}
	
第四步：服务中调用，引入以上定义的接口
 
	@Autowired
	private SmsClient smsClient;
	
	//feign调用
	ResponseResult result = smsClient.sendSms(smsSendRequest);
	
	
说明：
1.被@FeignClient(name = "service-sms",fallback = SmsClientFallback.class)修饰的接口的bean是如何创建的？我们调了一个接口的方法就能访问一个服务，我们需要知道这个实例bean是如何创建的。

2.调用FeignServiceClient对象的网络请求相关的函数时，OpenFeign是如何发送网络请求的。feign是怎么构造的，构造后是怎么去调的？

@EnableFeignClients三作用∶
一是引入FeignClilentsRegistrar;
二是指定扫描FeignClient的包信息，就是指定FeignClient接口类所在的包名;
三是指定FeignCliet接口类的自定义配置类
过程：注册bean，实例化bean，发送网络请求
















==========================================
#feign和ribbon的区别?查看下SynchronousMethodHandler
feign是














=============================================
#熔断hystrix
    假设D、E、F依赖B和C，B和C；依赖A，A因为某种原因异常了，随之B和C也受影响，随之D、E、F跟着受影响，这个就叫做雪崩。
    在实际应用中，发生雪崩实际就是3个流程，服务不可用，别的服务来重试，重试会导致网络流量的增大，最后导致服务调用者不可用。
    
解决的问题:
    在分布式系统中，一个服务依赖多个服务，可能存在某个服务调用失败，比如超时，异常，如何保证在某些服务故障时，对整个服务没有影响
	 
熔断后续∶
    出现错课后用allback返回错误的处理信息，或者兜底数据。
    
熔断∶
    为隔断服务调用者和异常服务提供者防止服务雪崩的现象，提供了一种保护措施。

降级∶
    为了在整体资源不够时，适当放弃部分服务，将主要的资源投放到核心服务中，待渡过难关之后，再重启已关闭的服务，保证了系统核心服务的稳定。
    
共同点∶
  1、为了防止系统崩遗。保证主要功能的可用性和可靠性。
  2、用户体验到某些功能不能用。

不同点∶
  1、熔断由下级故障触发，主动惹祸。
  2、降级由调用方从负荷角度触发，无辜被抛弃。

总结：
   熔断∶多次失败，而被暂时性忽略，短期不再尝试使用。
   降级∶主逻辑失败采用备用逻辑的过程。

   
断路器工作过程：见断路器开关时序图.png
    调用时，失败的次数在一段时间内达到一定的值，断路器就会跳闸开关打开，不会真实调用接口，这样就避免了服务调用者在服务提供者不可用的时候发送请求，减少了资源的消耗，保护了所有的调用者。
    好了怎么办？一段时间后，断路器会变成半开的状态，此时允许调用，当调用成功的比例达到一定的程度时，断路器关闭。如果成功的次数没有达到一定程度，断路器重新变成打开状态。
    
    
#####ribbon的熔断  
第一步：调用方api-passenger的pom加依赖

       <!-- 引入hystrix依赖 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
		</dependency>

第二步：启动类添加注解@EnableCircuitBreaker

    @EnableEurekaClient
	 @SpringBootApplication
	 @EnableFeignClients
	 @EnableCircuitBreaker
	 @ComponentScan(excludeFilters = {
			@ComponentScan.Filter(type = 	FilterType.ANNOTATION,value=ExcudeFeignConfig.class)
	})
	public class ApiPassengerApplication {

		public static void main(String[] args) {
			SpringApplication.run(ApiPassengerApplication.class, args);
		}
	
	
		@Bean
		@LoadBalanced
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	
	}
	
第三步：方法添加@HystrixCommand(fallbackMethod = "sendFail")，以及备用逻辑方法sendFail

    
    /**
	 * 发送验证码
	 * @param shortMsgRequest
	 * @return
	 *
	 */
	@HystrixCommand(fallbackMethod = "sendFail")
	@PostMapping("/verify-code/send")
	public ResponseResult verifyCodeSend(@RequestBody ShortMsgRequest shortMsgRequest) {
		String phoneNumber = shortMsgRequest.getPhoneNumber();
		//校验手机号
		if(StringUtils.isBlank(phoneNumber)) {
			return ResponseResult.fail(CommonStatusEnum.PHONENUMBER_EMPTY.getCode()	, CommonStatusEnum.PHONENUMBER_EMPTY.getValue());
		}
		Boolean phoneFlag = PhoneUtil.isPhone(phoneNumber);
		if(!phoneFlag) {
			return ResponseResult.fail(CommonStatusEnum.PHONENUMBER_ERROR.getCode()	, CommonStatusEnum.PHONENUMBER_ERROR.getValue());
		}
		//获取验证码-正常代码
      //String code = verificationCodeService.getCode(phoneNumber);
		String code = "010101";
		log.info("service-verification-code 返回的验证码："+code);
		return shortMsgService.send(phoneNumber, code);
		
	}
	
	public ResponseResult sendFail(ShortMsgRequest shortMsgRequest) {
		//备用逻辑
		return ResponseResult.fail(-1, "熔断");
	}
	
#####feign的熔断
第二步：yml问价添加熔断配置
feign:
  compression:
    request:
      mime-types:
      - text/xml
      min-request-size: 2048
  #开启feignhystrix
  hystrix:
    enabled: true 
    
第三步：在调用feign的时候

	@Autowired
	private SmsClient smsClient;

    //feign调用
    ResponseResult result = smsClient.sendSms(smsSendRequest);

写一个熔断实现类    
    
    @Component
    public class SmsClientFallback implements SmsClient {

	//	@Autowired
	//	private StringRedisTemplate redisTemplate;
	
		@Override
		public ResponseResult sendSms(SmsSendRequest smsSendRequest) {
			System.out.println("不好意思，我熔断了");
		
	//		String key = "service-sms";
	//		String noticeString = redisTemplate.opsForValue().get(key);
	//		if(StringUtils.isBlank(noticeString)) {
	//			发送短信，或者调用电话接口
	//			System.out.println("通知别人我熔断了");
	//			redisTemplate.opsForValue().set(key, "notice", 30, TimeUnit.SECONDS);
	//			
	//		}else {
	//			System.out.println("通知过了，我先不通知了");
	//		}
		
			return ResponseResult.fail(-3, "feign熔断");
		}

	}

接口上添加fallback = SmsClientFallback.class
	
	@FeignClient(name = "service-sms",fallback = SmsClientFallback.class)
	public interface SmsClient {
		/**
	 	* 按照短信模板发送验证码
	 	* @param smsSendRequest
	 	* @return
	 	*/
		@RequestMapping(value="/send/alisms-template", method = RequestMethod.POST)
		public ResponseResult sendSms(@RequestBody SmsSendRequest smsSendRequest);
	}
	
	
第四步：熔断后通知相关开发人员，通知一般加到熔断的fallback函数里，可以每30秒发一次短信，可以用redis来控制，某个key设置值，设置失效时间为30秒，如果key里有值就不再发短信了，如果key没有值就继续。

第一步：pom文件添加redis的jar包

	    <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>


第二步：yml文件添加配置
 #应用名称及验证账号
spring: 
  application: 
    name: api-passenger
    admin:
      enabled: true
  redis:
    database: 0
    host: 192.168.153.129
    port: 6379
    timeout: 2000
 
 第三步：StringRedisTemplate注入，方法中调用
 
    @Autowired
	 private StringRedisTemplate redisTemplate;
	 
	 @Override
	 public ResponseResult sendSms(SmsSendRequest smsSendRequest) {
		System.out.println("不好意思，我熔断了");
		
		String key = "service-sms";
		String noticeString = redisTemplate.opsForValue().get(key);
		if(StringUtils.isBlank(noticeString)) {
			//发送短信，或者调用电话接口
			System.out.println("通知别人我熔断了");
			redisTemplate.opsForValue().set(key, "notice", 30, TimeUnit.SECONDS);
			
		}else {
			System.out.println("通知过了，我先不通知了");
		}
		
		return ResponseResult.fail(-3, "feign熔断");
	}
	 
第四步：实验，调用接口，将sms-service服务停掉，制造熔断情景
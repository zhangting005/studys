一、简介：这个服务是eureka的服务端，可以通过eureka之间的相互注册形成集群
      
二、搭建步骤：
1.pom文件添加：
        <!-- eureka 服务端 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>

2.yml文件
server: 
  port: 7901
  
#应用名称及验证账号
spring: 
  application: 
    name: eureka
  security: 
    user: 
      name: root
      password: root
      
#注册中心
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    #多个保证其中有一个down掉了，另外两个还能提供服务:注册中心的服务端集群
    service-url:                      
      defaultZone: http://root:root@eureka-7901:7901/eureka/,http://root:root@eureka-7902:7902/eureka/,http://root:root@eureka-7903:7903/eureka/
  instance: 
    hostname: eureka-7901
#  server: 
#    enable-self-preservation: false #关闭自我保护
#    eviction-interval-timer-in-ms: 5000 #清理间隔时间

3.启动类添加@EnableEurekaServer
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}
}

三、客户端使用
1.pom文件添加
        <!-- eureka客户端 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
2.yml添加
#注册中心
eureka: 
  client:
    #设置服务注册中心的URL：此处只需要注册到7901,7902和7903也会有
    service-url:                      
      defaultZone: http://root:root@eureka-7901:7901/eureka/
  instance: 
    hostname: localhost
    instance-id: api-passenger
    
2.1 eureka客户端api-passenger只有注册到7901，为什么7902和7903也会有？
    原因：7901向7902(和7903)注册，7902(和7903)就会同步7901注册中心的注册信息，所以在api-passenger服务中，只在7901注册了，7902和7903也会有，因为7901将服务注册到了7902和7903上：
  defaultZone: http://root:root@eureka-7901:7901/eureka/,http://root:root@eureka-7902:7902/eureka/,http://root:root@eureka-7903:7903/eureka/

2.2如果7901挂掉了，7902和7903上还会有api-passenger注册的服务信息吗？
   回答：没有，因为7901挂掉了，7902和7903同步的数据就没有了，但是会有延迟
    
3.启动类添加@EnableEurekaClient
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@ComponentScan(excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION,value=ExcudeFeignConfig.class)
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

4.多个eureka集群实际运用方式
问题：如果启动3个是不是要起3个呢？
回答：启动3个，不用写3个项目，只需要写3个不同的配置文件。比如我们在docker中启动时，会用同样的一份代码，启动时会传入配置文件的参数，指定某个配置文件去启动（启动的命令中指定的配置文件不一样）。

5.eureka服务注册原理
    如果服务消费者和服务提供者都是eureka的客户端，中间有一个eureka的注册中心，客户端（服务提供者）把服务注册到注册中心后，供另一个客户端（服务消费者）调用。
   
    注册：客户端起来后会将服务注册到注册中心，注册的过程是：在发起服务注册时会将自身的服务实例的元数据封装在一个叫InstanceInfo中，然后将InstanceInfo发送到eureka的注册中心，注册中心在收到注册请求（InstanceInfo）后，存在自己本地（服务中心的注册表中），供其他的eureka来调用。
    续约：客户端进行续约，第一次服务起来后，把服务注册到注册中心，注册完后还要续约，续约就是客户端完成注册后，需要定时向服务端发送心跳请求，默认30秒一次，维持自己在注册中心租约的有效性，告诉注册中心，客户端现在还活着，还是有效的，如果client在注册后没有续约，没有发心跳，没有主动下线，或者服务崩溃，或者网络异常等原因，那么服务的状态此时就为不可知状态，不能保证从该服务中取到响应。
   下线：服务在销毁时向注册中心发送下线请求，注册中心会清除自己注册表中关于这个应用的租约，避免发生无效的服务调用。
   
   服务将自身注册到注册中心后，是要供别的服务调用的，其他服务（eureka的客户端）调用时，会去服务的注册中心拉取注册列表。拉取分为全量拉取和增量拉取。
    
  eureka自我保护机制：在server端和client端都会实现的，假定在某种特定的情况下（网络异常等），eureka的客户端和服务端无法进行通信，此时客户端无法向向服务端发送注册和续约的请求，server中可能因注册表中的实例的租约出现大量过期面临被剔除的风险，一旦client没有续约（可能是网络异常等因素）就剔除，这样显然是不合理的，因为client会拉取注册列表缓存到本地，但是client端的服务是没有异常的（系统还是可用的），只是没有去注册中心续约，注册中心不能把client踢掉，这种情况下，server的节点会进入自我保护模式，自我保护模式就是注册表中的信息不再被剔除，等通信网络正常后再退出自我保护模式，这个是自我保护模式server端的实现。
  这种模式在client端也有实现，如果client端向eureka注册失败，将快速超时并尝试与别的server（别的server成功会把信息同步到所有server集群中的节点）进行通信。
  自我保护机制的触发条件：当每分钟的心跳次数少于某个值n，就会开启自我保护模式。这个值n的计算:
                                     实例数*2*续租百分比（0.85可配置，不配置有默认值）= n
  自我保护机制打开时才会触发，如果把自我保护机制关闭了，就不会触发了。生产中一般是会关掉自我保护机制的，服务不可用就踢掉。然后有一些告警机制告诉服务不可用了，工作人员去查看。

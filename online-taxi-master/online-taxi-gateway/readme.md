#为什么用网关?
解决的问题∶
（1）客户端会多次请求不同微服务，增加了客户端复杂性
（2）认证复杂，每个服务都需要独立认证
（3）难以重构，多个服务可能将会合并成一个或拆分成多个
（4）安全性，微服务不需要向外暴露，通过物理网络隔离。 只有网关开放对外的ip。

#网关功能
统一接入∶智能路由，AB测试、灰度测试，日志埋点流量监控∶限流处理
安全防护∶鉴权，网络物理隔离

AB测试：为一个目标制定两个方案，让一部分用户使用A方案，一部分用户使用B方案，记录下用户的使用情况，看那哪个方案更符合需要达成的目标。
灰度测试：某个产品功能正式发布之前，先选定一部分人，将功能开放出去，然后再逐步扩大功能使用者的数量。减弱功能出现问题带来的影响。
日志埋点：例如一个发短信的功能，可以记录下进来的参数，和返回的参数，还有中间调用的时长，一天用户访问记录之类的，可以在网关记录下来，不用再每个微服务里写，这样方便统一日志的处理，比如做日志分析，从网关的日志就可以拿到。比如某个接口每天的访问量，访问时长，请求接口时带入的用户信息是安卓还是IOS之类的。
流量监控：限流处理，比如访问网关的流量过大了，某借口哦只支持1000QPS，突然过来1100，那么多出来的100就不要了，因为如果放进来，系统可能会挂掉。
安全防护：对访问系统用户的身份认证和检测。
网络物理隔离：把所有服务隔离在局域网内，只把网关暴露出去。这是一种物理级别上的保护。

#网关介绍及应用场景
zuul∶Netfix出品，和eureka，ribbon， hystrix配合使用，自家况总。

#网关基本使用
准备两个下游服务
api-passenger:127.0.0.1:9001/api-passenger-gateway-test/hello
service-sms:127.0.0.1:8003/service-sms-gateway-test/hello

步骤一：pom文件添加依赖-网关也要注册到注册中心，是eureka的客户端

	   <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>

		<!--zuul -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
		</dependency>
		
步骤二：启动类添加注释 @EnableEurekaClient @EnableZuulProxy

	@SpringBootApplication
	@EnableEurekaClient
	@EnableZuulProxy
	public class OnlineTaxiGatewayApplication {

		public static void main(String[] args) {
			SpringApplication.run(OnlineTaxiGatewayApplication.class, args);
		}

	}

步骤三：yml添加配置
server:
  port: 9000
spring:
  application:
    name: online-taxi-gateway
 #注册中心
eureka: 
  client:
    #设置服务注册中心的URL
    service-url:                      
      defaultZone: http://root:root@eureka-7901:7901/eureka/
  instance: 
    hostname: localhost
    instance-id: online-taxi-gateway 
    
步骤四：将两个下游服务的访问改为网关访问
准备两个下游服务
api-passenger:127.0.0.1:9001/api-passenger-gateway-test/hello
service-sms:127.0.0.1:8003/service-sms-gateway-test/hello

改为用网关访问：
127.0.0.1:9000/api-passenger/api-passenger-gateway-test/hello  /api-passenger是服务名
127.0.0.1:9000/service-sms/service-sms-gateway-test/hello

#自定义路由
zuul:
  routes:
    service-sms: /gate-way/sms/**

总结：
127.0.0.1:9000/gate-way/sms/service-sms-gateway-test/hello 等价
127.0.0.1:9000/service-sms/service-sms-gateway-test/hello

#忽略路由（禁止用服务名访问）：URL中有api-passenger就不能访问了
zuul:
  routes:
    service-sms: /gate-way/sms/**
    #忽略
  ignored-services:
  - api-passenger
  
#请求头过滤
在实际开发中zuul会将请求头里某些信息如Cookie，Set-Cookie，Authorization给过滤掉，不会传给后面的微服务

举例调用：127.0.0.1:9000/api-passenger/api-passenger-gateway-test/test-token
Header中放：
    Content-Type:application/json
	token:token-11
	Cookie:cookie-22
控制台打印：
       乘客api：token：token-11
	乘客api：cookie：null
	
解决方法，yml中添加配置sensitive-headers:
zuul:
  routes:
    service-sms: /gate-way/sms/**
    #忽略
  ignored-services:
  - api-passenger
  
  ignored-patterns:
  - /*-passenger/**

  #以下配置为空表示：所有请求头都透传到后面微服务。
  sensitive-headers:

 #zuul鉴权过滤器执行流程
 http请求 ---> pre filter ---> routing filter ---> post filter ---> 返回
 参照AuthFilter.class
 
 拦截举例：127.0.0.1:9000/api-passenger/api-passenger-gateway-test/hello
 没有Authorization:1234报401被拦截
 Content-Type:application/json
 token:token-11
 Cookie:cookie-22
 Authorization:1234
 
#网关限流
实际中，用的是谷歌的一个技术 参照RateFilter.class
 限流举例：127.0.0.1:9000/api-passenger/api-passenger-gateway-test/hello
 没有Authorization:1234报401
 Content-Type:application/json
 token:token-11
 Cookie:cookie-22
 Authorization:1234
说明：类中设置每秒一个令牌，接口调用快一点，就会报429Too Many Requests

#网关集群搭建
gateway+(nginx+keepalived)，nginx+keepalived相当于nginx的高可用
将gateway当成一个普通服务，通过nginx做负载。
nginx做成高可用。

#链路追踪及sleuth
解决错综复杂的服务调用中链路的查看。排查慢服务。
最朴素∶通过调用前后加时间戳，实际用链路追踪。
链路追综通过流水号跟综链路

sleuth功能：做日志埋点
项目重启后，发起一次调用工日志有∶
[online-tax-gateway,b2c9be544be51e48,b2c9be544be51e48,false]
[服务名称，traceId(一条请求调用链中唯一ID)，spanID(基本的工作单元，获取数据等)，是否让zipkin手机和展示此信息]
必须写服务名

步骤一：pom文件添加依赖（要做链路追踪的服务都要加）

		<!-- 引入sleuth依赖 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-sleuth</artifactId>
		</dependency> 
		
步骤二：通过网关发送短信
接口：127.0.0.1:9001/api-passenger/sms/verify-code/send
方式：post
参数：{
         "phoneNumber":"15971398388"
      }
      
控制台打印：
[online-taxi-gateway,33c0c6bfcb1cdc5c,33c0c6bfcb1cdc5c,false]
[api-passenger,33c0c6bfcb1cdc5c,e68b9506d8b9f0a2,false]
[service-sms,33c0c6bfcb1cdc5c,e801b652781b8d0c,false]

#链路追踪zipkin
链路追综zipkin
zipkin:
大规模分布式系统的APM工具（Application Performance Mananement，基于Google Dapper(大规模分布式跟踪系统)的基础实现，和sleuth结合可以提供可视化web界面分析调用链路耗时情况
同类产品∶美团点评∶cat，阿里鹰眼
功能∶
Collector、Storage、Restful API、Web UI组成

键路追踪sleuth+zipkin
sleuth收集跟踪信息通过http请求发送给zipkin server，zipkin将跟踪信息存储，以及提供RESTful API接口，zipkin ui通过调用api进行数据展示。
默认内存存储，可以用mysql，ES等存储。

步骤一：运行zipkin的jar包- java -jar zipkin.jar

步骤二：pom文件添加zipkin依赖（将sleuth注释掉，因为已经包含了sleuth）
        
       <!-- zipkin -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-zipkin</artifactId>
		</dependency>
		
步骤三：yml添加zipkin配置
spring:
  application:
    name: online-taxi-gateway
    
  #zipkin
  zipkin:
    base-url: http://localhost:9411/
    
步骤四：访问地址：http://localhost:9411/zipkin/

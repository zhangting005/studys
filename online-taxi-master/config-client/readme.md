#配置中心动态刷新-结合消息总线实现配置实时刷新
·步骤一∶

       <!-- 服务监控开启refresh 端口 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		<!-- 总线 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-bus-amqp</artifactId>
		</dependency>
		
步骤二:
·1、依次启动eureka(注册中心服务端)， config-server(配置中心服务端,同时也是eureka客户端)，service-client8011(配置中心客户端,同时也是eureka客户端), service-client8012(配置中心客户端,同时也是eureka客户端)。

·配置中心∶http://localhost:6001/service-verification-code-qa.yml

·短信服务∶http∶//localhost8011/config/env 

·刷新服务∶http://localhost:8011/git-webhook/bus-refresh(或8012)

总结：如果在GitHub上改变了配置，配置中心server端会跟着改变，但是client不会，如果不想重启服务，那么需要调用刷新服务的接口(http://localhost:8011/git-webhook/bus-refresh)。
http://localhost:6001/service-verification-code-qa.yml访问会自动改变
http://localhost:8011/config/env 访问不会自动改变

步骤三:配置rabbitmq
spring: 
  application: 
    name: service-verification-code
 #消息
  rabbitmq:
    host: 192.168.153.129
    port: 5672
    username: guest
    password: guest
    
 #配置中心
  cloud: 
    config:
      discovery:
        # 表示，使用discovery中的config server，而不是 自己指定uri，默认false。
        enabled: true
        service-id: config-server
      profile: qa
management:
  endpoints:
    web:
      exposure:
        #yml加双引号，properties不用加，暴露出去接受动态刷新的请求
        include: "*"

#问题：如果有
1.GitHub
2.Config-Server
3.Config-Client8011
4.Config-Client8012
5.rabbitmq

如果更新GitHub上的配置，配置中心Config-Server会自动感知到，访问配置中心会查询到最新的配置：
    http://localhost:6001/service-verification-code-qa.yml
    
此时，调用刷新服务的接口(http://localhost:8011/git-webhook/bus-refresh)
只调用8011，那么访问http∶//localhost8011/config/env会更新，但是访问http∶//localhost8012/config/env不会更新。

上面8011和8012同时更新，是因为加入了rabbitmq，8011向rabbitmq发送消息，8012监听订阅rabbitmq的消息
#配置中心-客户端调用

第一步：pom文件添加依赖(也要注册到eureka客户端)

		<!-- 配置中心客户端：config-client -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-client</artifactId>
		</dependency>
		<!-- eureka客户端 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		
第二步：配置中心客户端配置bootstrap.yml

 #应用名称，配置文件名，此时:service-verification-code-qa.yml
spring: 
  application: 
    name: service-verification-code
  cloud: 
    config:
      discovery:
        enabled: true
        service-id: config-server
      profile: qa
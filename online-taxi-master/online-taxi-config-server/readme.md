#配置中心
·统一管理配置∶避免配置分散，不好管理，更新复杂。
·安全需要∶有些配置，不能让开发知道。

·产品∶
·阿里云ACM
· Spring Cloud的config-server

步骤一:准备好配置仓库
配置中心
·准备工作∶
·github上新建仓库∶
·https://github.com/yueyi2019/online-taxi-config-profleait 
·放入验证码服务的配置∶
·service-verification-code.yml

第二步：pom文件添加依赖（也要注册到eureka）

       <!-- 配置中心服务端：config-server -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-server</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		
第三步：yml添加配置

 #应用名称及验证账号
spring: 
  application: 
    name: config-server
    
  cloud:
    config:
      server:
        
        git:
        #https://github.com/yueyi2019/online-taxi-config-profile.git
          uri: https://github.com/yueyi2019/online-taxi-config-profile
          username: 
          password: 
                      #默认是秒，因为git慢
          timeout: 15

第四步：启动类添加注解@EnableConfigServer

	@EnableConfigServer
	@SpringBootApplication
	public class OnlineTaxiConfigServerApplication {

		public static void main(String[] args) {
			SpringApplication.run(OnlineTaxiConfigServerApplication.class, args);
		}
	}
	
第五步：验证
·启动eureka 
·启动config-server 
·访问∶配置中心某个配置文件：http://localhost:6001/service-verification-code.yml


    
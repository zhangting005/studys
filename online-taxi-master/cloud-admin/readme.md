步骤一:
pom文件中添加依赖
说明：cloud-admin是健康检查的服务server端，它的client端，是server端要去检测健康情况的服务，如config-client就是client端。

    	<!-- Admin 服务 -->
		<dependency>
			<groupId>de.codecentric</groupId>
			<artifactId>spring-boot-admin-starter-server</artifactId>
		</dependency>
		<!-- Admin 界面 -->
		<dependency>
			<groupId>de.codecentric</groupId>
			<artifactId>spring-boot-admin-server-ui</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>

		<!-- WEB相关依赖：排斥Tomcat，加入jetty -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-tomcat</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jetty</artifactId>
		</dependency>
	
步骤二:需要健康检查的服务添加依赖(如config-client)

       <!-- 服务监控开启refresh 端口 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		
步骤三:需要健康检查的服务(如config-client)把所有外部访问的接口暴露出去	

management:
  endpoints:
    web:
      exposure:
        #yml加双引号，properties不用加
        include: "*" 
        
        

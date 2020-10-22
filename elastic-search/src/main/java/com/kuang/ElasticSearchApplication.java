package com.kuang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
* @author: EX-ZAHNGTING011
* @Date: 2020-3-12 15:14
* Description:
*/
@SpringBootApplication
public class ElasticSearchApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }

}

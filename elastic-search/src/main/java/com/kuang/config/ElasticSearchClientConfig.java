package com.kuang.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @author: EX-ZAHNGTING011
* @Date: 2020-10-22 13:51
* Description:配置文件，相当于xml
*/
@Configuration
public class ElasticSearchClientConfig {

    /**
     * id相当于方法名，返回值绑定RestHighLevelClient.class
     * 两步骤：
     * 1.找对象
     * 2.放到spring中待用
     * 3.如果是springboot就要分析源码：xxx AutoConfiguration , xxx Properties
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        return client;
    }

}
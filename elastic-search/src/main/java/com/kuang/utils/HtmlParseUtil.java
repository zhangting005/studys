package com.kuang.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;

/**
* @author: EX-ZAHNGTING011
* @Date: 2020-10-22 18:41
* Description:
*/
public class HtmlParseUtil {
    public static void main(String[] args) throws IOException {
        //获取请求 https://search.jd.com/Search?keyword=java
        //前提：需要联网
        String url = "https://search.jd.com/Search?keyword=java";
        //解析网页(Jsoup返回Document就是浏览器Document对象)
        Document document = Jsoup.parse(new URL(url), 10000);
        //所有你在JS中使用的方法这里都能用
        Element element = document.getElementById("J_searchWrap");
        System.out.println(element.html());
    }
}

package com.online.taxi.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
/**
 * 鉴权filter
 * @author yueyi2019
 */
@Component
public class AuthFilter extends ZuulFilter {

	/**
	 * 过滤器是否生效
	 */
	@Override
	public boolean shouldFilter() {
		//获取上下文
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		
		String uri = request.getRequestURI();
		System.out.println("来源uri："+uri);
		//只有此接口/api-passenger/api-passenger-gateway-test/hello才被拦截
		//实际中，这些URL会写在配置文件或者数据库里
		String checkUri = "/api-passenger/api-passenger-gateway-test/hello";
		if(checkUri.equalsIgnoreCase(uri)) {
			//true打开过滤器，如果相等，过滤器生效，生效的代码在一下run()中
			return true;
		}
		
		return false;
	}

	@Override
	public Object run() throws ZuulException {
		System.out.println("拦截");
		//获取上下文
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		
		String token = request.getHeader("Authorization");
		//如果token是1234，过
		String defaultToken = "1234";
		if(StringUtils.isNotBlank(token) && defaultToken.equals(token)) {
			System.out.println("校验通过");
		} else {
			requestContext.setSendZuulResponse(false);
			requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
		}
		return null;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		//PRE_TYPE前置过滤器
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		//order的值越小，越先执行
		return 4;
	}

}
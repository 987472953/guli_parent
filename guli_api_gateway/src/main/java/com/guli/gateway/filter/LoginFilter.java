package com.guli.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class LoginFilter extends ZuulFilter {

    /**
     * 过滤器类型
     * 前置 路径中 微服务后 错误
     * PRE_TYPE
     * ROUTE_TYPE
     * POST_TYPE
     * ERROR_TYPE
     *
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器级别 顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 过滤器是否生效
     * return false为放行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        //鉴权URL(项目中我们会在数据库或缓存中取出ACL列表)
        String authUrl = "/vod-api/vod/playauth/";
        //请求路径
        String requestURI = request.getRequestURI();

        if(requestURI.contains(authUrl)){
            return true;
        }

        return false;
    }

    /**
     * 过滤器生效执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {

        System.out.println("过滤器生效。。。。。。。。。。。。。");
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        String token = request.getHeader("token");

        if(StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }

        if(StringUtils.isEmpty(token)){
            //设置false，请求不会继续下发到下游服务器
            currentContext.setSendZuulResponse(false);
            //设置响应状态码401
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}

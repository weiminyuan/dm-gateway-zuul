package com.example.demo.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FilterConstants 常量类
 *
 * @Author: yuanweimin
 * @Date: 2018/12/4 17:14
 */
@Component //注册
public class SecondFilter extends ZuulFilter{
    /**
     * 类型
     * pre 前处理
     * rouse    环绕处理
     * post 后处理
     * error 全处理
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 同类型优先级
     * @return
     */
    @Override
    public int filterOrder() {
        return 2;   //排序越小优先级越高
    }

    /**
     * 过滤器是否起作用
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //获取上一级的状态
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.sendZuulResponse();
    }

    /**
     *
     * @return
     */
    @Override
    public Object run(){
        //请求对象
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        String key = request.getParameter("key");
        if ("2".equals(key)){
            ctx.setSendZuulResponse(false);//不会走后面的过滤器
            ctx.setResponseStatusCode(401);//返回状态
            ctx.setResponseBody("{\"code\":\"401\",\"message\":\"SecondFilter拦截\"}");
            return "访问被拒绝SecondFilter";
        }
        return "通过SecondFilter";
    }


}

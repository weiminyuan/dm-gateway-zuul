package com.example.demo.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FilterConstants 常量类
 *
 * @Author: yuanweimin
 * @Date: 2018/12/4 17:14
 */
@Component //注册
@RefreshScope //重写注入
public class PreFilter extends ZuulFilter{
    @Value("${token}")
    private boolean token;

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
        return 0;   //排序越小优先级越高
    }

    /**
     * 过滤器是否起作用
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
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
        response.setCharacterEncoding("utf-8");
        String tokens = request.getHeader("token");
        String key = request.getParameter("key");
        //token登录信息（可以在redis里面存在）
        System.out.println("========================"+token+"=========================");
        if(token){
            System.out.println("ok");
        }else {
            System.out.println("fail");
        }

        if (StringUtils.isEmpty(tokens) || "1".equals(key)){
            ctx.setSendZuulResponse(false);//不会走后面的过滤器
            ctx.setResponseStatusCode(401);//返回状态
            ctx.setResponseBody("{\"code\":\"401\",\"message\":\"未登录PreFilter拦截\"}");
            return "访问被拒绝";
        }
        return "通过pre";
    }


}

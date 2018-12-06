package com.mmall.controller.common;

import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionExpireFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //获取token
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        //如果token不为空或‘’
        if (!StringUtils.isNotEmpty(loginToken)){
            //通过token去redis中获取登录信息
            String userJoonStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJoonStr, User.class);
            if (user != null){}
            //user不为空，则重置session的时间，调用expire命令
            RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}

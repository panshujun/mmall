package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.rmi.runtime.Log;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    public final static String COOKIE_DOMAIN =".mmall.com";
    public final static String COOKIE_NAME ="mmall_login_token";

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks =request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                log.info("read cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    ck.getValue();
                }
            }
        }
        return null;
    }
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck =new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在跟目录
        ck.setHttpOnly(true);
        //单位是秒
        //如果这个maxage不设置得话，cookie就不会写入硬盘，而是写在内存，只在当前页面有效
        ck.setMaxAge(60 * 60 *24 * 365);  //如果是-1 代表永久
        log.info("write cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }

    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cks =request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
               if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                   ck.setDomain(COOKIE_DOMAIN);
                   ck.setPath("/");
                   ck.setMaxAge(0);   //设置为0，表示删除此cookie
                   ck.setHttpOnly(true);
                   log.info("del cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                   response.addCookie(ck);
                   return;
               }
            }
        }
    }
}

package com.jingsky.customer.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Request Interceptor unregistered
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = Logger.getLogger(AuthInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer requestUrl = request.getRequestURL();
        //check session
        boolean hasSession = request.getSession().getAttribute("name") != null;
        if (hasSession) {
            return true;
        } else {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("status","failure");
            jsonObject.put("msg","unlogin");
            response.getWriter().print(jsonObject.toString());
            return false;
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

package com.DLY.interceptor;

import com.DLY.common.BaseContext;
import com.DLY.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截的url"+request.getRequestURI());
        Object phone = request.getSession().getAttribute("phone");
        if(phone!=null){
            BaseContext.setCurrentId((Long)request.getSession().getAttribute("userID"));
            return true;
        }else{
            //没有登入返回未登入结果
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

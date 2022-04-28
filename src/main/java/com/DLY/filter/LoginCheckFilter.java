package com.DLY.filter;

import com.DLY.common.BaseContext;
import com.DLY.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebFault;
import java.io.IOException;

//过滤器的名字  和过滤的路径
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器支持通配符写法   ==>    /**
    private static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;  //向下转型
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //log.info("拦截到请求:{}",request.getRequestURI());
        //获取本次请求的uri
        String requestURI = request.getRequestURI();
        //判断请求是否需要处理   静态资源和登入退出不需要处理
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/static/**",
                "/common/**",
                "/user/**",
                "/category/**",
                "/shoppingCart/**",
                "/order/**",
                "/addressBook/**",
                "/dish/**",
                "/setmeal/**",
                "/order/**"
        };
        boolean check=check(urls,requestURI);   //路径匹配情况

        //不需要的直接放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        //判断登入状态
        Object empId = request.getSession().getAttribute("employee");
        if(empId !=null){
            //设置当前正在操作的用户
            BaseContext.setCurrentId((Long) empId);
            filterChain.doFilter(request,response);
            return;
        }

        log.info(requestURI);
        //没有登入返回未登入结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI){
        for (String url:urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){   //有一个路径匹配上就可以返回true
                return true;
            }
        }
        return false;
    }
}

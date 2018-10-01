package com.common.web.filter;

import com.string.widget.util.ValueWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * 支持ajax 跨域访问
 * @author huangweii
 * 2015年6月7日
 */
public class SimpleCORSFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        //优先级 request > header
        String allOrigin = request.getParameter("allowOrigin");
        String message = "############## allOrigin :" + allOrigin;
        log.warn(message);
        System.out.println(message);
        if (ValueWidget.isNullOrEmpty(allOrigin)) {
            allOrigin = request.getHeader("Origin");
            if (ValueWidget.isNullOrEmpty(allOrigin)) {
                allOrigin = "*";
            }
        }
        response.setHeader("Access-Control-Allow-Origin", allOrigin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        String allCookie = request.getParameter("allowCookie");
        if (!ValueWidget.isNullOrEmpty(allCookie) && "true".equalsIgnoreCase(allCookie)) {//允许客户端带cookie
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        chain.doFilter(req, res);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}

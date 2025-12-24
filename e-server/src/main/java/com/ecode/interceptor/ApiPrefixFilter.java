package com.ecode.interceptor;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiPrefixFilter implements Filter {

    /**
     * 解决前后端不分离时去除/api
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();

        // 如果路径以 /api 开头
        if (path.startsWith("/api")) {
            // 截取掉 /api (长度为4)
            String newPath = path.substring(4); 
            // 转发(Forward)到新路径。
            // 注意：Forward 会让请求重新走一遍 DispatcherServlet，这正是我们想要的
            req.getRequestDispatcher(newPath).forward(request, response);
        } else {
            // 其他路径（如 /index.html, /doc.html）直接放行
            chain.doFilter(request, response);
        }
    }
}
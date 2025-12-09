package com.ecode.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * SPA (Single Page Application) 前端路由支持配置类。
 * <p>
 * 解决前后端分离项目合并部署时，刷新非根路径页面报 404 的问题。
 * <p>
 * <h3>原理说明：</h3>
 * 单页应用（如 Vue/React）使用 HTML5 History API 管理路由时，
 * 类似于 {@code /myClass} 这样的路径是前端虚拟路由。
 * 当用户刷新页面时，浏览器向服务器请求该路径，但服务器文件系统中并不存在对应的资源。
 * <p>
 * 本配置通过拦截 HTTP 404 错误，将其指向应用入口 {@code index.html}，
 * 让前端路由脚本接管后续的页面渲染。
 *
 * @author 竹林听雨
 * @version 1.0
 */
@Configuration
public class Spa404Config {

    /**
     * 自定义 Web 服务器工厂配置。
     * <p>
     * 注册一个全局的错误页面映射：当发生 {@link HttpStatus#NOT_FOUND} (404) 错误时，
     * 服务器内部转发到 {@code /index.html}。
     *
     * @return WebServerFactoryCustomizer 定制器实例
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            // 创建错误页映射：404 -> /index.html
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");

            // 将其添加到容器配置中
            factory.addErrorPages(error404Page);
        };
    }
}
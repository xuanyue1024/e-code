package com.ecode.config;

import com.ecode.interceptor.JwtTokenStudentInterceptor;
import com.ecode.interceptor.JwtTokenTeacherInterceptor;
import com.ecode.interceptor.JwtTokenUserInterceptor;
import com.ecode.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;
    @Autowired
    private JwtTokenTeacherInterceptor jwtTokenTeacherInterceptor;
    @Autowired
    private JwtTokenStudentInterceptor jwtTokenStudentInterceptor;


    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        //登录要求拦截
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**","/auth/**")
                .excludePathPatterns(
                        "/user/register","/user/login","/user/ai/**",
                        "/auth/passkey/assertion"
                        );
        //教师请求拦截
        registry.addInterceptor(jwtTokenTeacherInterceptor)
                .addPathPatterns("/teacher/**");
        //学生请求拦截
        registry.addInterceptor(jwtTokenStudentInterceptor)
                .addPathPatterns("/student/**");
    }


    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    /**
     * 扩展Spring MVC框架的消息转化器
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自己的消息转化器加入容器中
        converters.add(0,converter);

        //需要追加byte，否则springdoc-openapi接口会响应Base64编码内容，导致接口文档显示失败
        // https://github.com/springdoc/springdoc-openapi/issues/2143
        //https://doc.xiaominfo.com/docs/faq/v4/knife4j-base64-response
        // 解决方案
        converters.add(0,new ByteArrayHttpMessageConverter());
    }

}

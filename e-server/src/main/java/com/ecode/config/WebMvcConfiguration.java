package com.ecode.config;

import com.ecode.interceptor.JwtTokenStudentInterceptor;
import com.ecode.interceptor.JwtTokenTeacherInterceptor;
import com.ecode.interceptor.JwtTokenUserInterceptor;
import com.ecode.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.function.Predicate;

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

    // 创建谓词，用于排除特定的包
    private final Predicate<RequestHandler> basePackageSelector = RequestHandlerSelectors.basePackage("com.ecode.controller");
    private final Predicate<RequestHandler> teacherPackageSelector = RequestHandlerSelectors.basePackage("com.ecode.controller.user.teacher");
    private final Predicate<RequestHandler> studentPackageSelector = RequestHandlerSelectors.basePackage("com.ecode.controller.user.student");
    //开发文档配置
    private final ApiInfo apiInfo = new ApiInfoBuilder()
            .title("ecode接口文档")
            .version("1.0")
            .description("ecode算法练习平台项目接口文档")
            .build();

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        //登录要求拦截
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/register","/user/login","/user/ai/**");
        //教师请求拦截
        registry.addInterceptor(jwtTokenTeacherInterceptor)
                .addPathPatterns("/teacher/**");
        //学生请求拦截
        registry.addInterceptor(jwtTokenStudentInterceptor)
                .addPathPatterns("/student/**");
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    //老师端
    @Bean
    public Docket docket1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("学生端接口")
                .apiInfo(apiInfo)
                .select()
                .apis(basePackageSelector.and(Predicate.not(teacherPackageSelector)))
                .paths(PathSelectors.any())
                .build();
    }
    //学生端
    @Bean
    public Docket docket2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("教师端接口")
                .apiInfo(apiInfo)
                .select()
                .apis(basePackageSelector.and(Predicate.not(studentPackageSelector)))
                .paths(PathSelectors.any())
                .build();
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
    }

}

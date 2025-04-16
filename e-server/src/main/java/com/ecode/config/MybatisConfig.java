package com.ecode.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ecode.json.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis配置类
 *
 * @author 竹林听雨
 * @date 2024/11/30
 */
@Configuration
public class MybatisConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 初始化核心插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 配置自定义的 MyBatis-Plus Jackson 类型处理器 Bean
     * <p>
     * 该 Bean 会 MyBatis-Plus 使用 自定义配置的主 ObjectMapper，
     * 确保 Jackson 的全局配置（如 Optional 序列化规则）对所有 MyBatis JSON 字段生效。
     * </p>
     *
     * <p><b>主要功能：</b></p>
     * <ul>
     *   <li>将 JacksonObjectMapper给到 MyBatis-Plus 的 JacksonTypeHandler</li>
     *   <li>确保数据库 JSON 字段的序列化/反序列化行为与 REST API 保持一致</li>
     *   <li>解决默认 JacksonTypeHandler 创建独立 ObjectMapper 导致配置失效的问题</li>
     * </ul>
     *
     * <p><b>关联配置：</b></p>
     * 需要配合 {@link JacksonObjectMapper#JacksonObjectMapper()}  构造方法使用，该方法提供了预配置的 ObjectMapper
     *
     * @return 配置好的 JacksonTypeHandler 实例
     * @see JacksonObjectMapper#JacksonObjectMapper()
     * @see com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
     * @since 1.0
     */
    @Bean
    public JacksonTypeHandler jacksonTypeHandler() {
        JacksonTypeHandler handler = new JacksonTypeHandler(Object.class);
        JacksonTypeHandler.setObjectMapper(new JacksonObjectMapper());  // 手动注入配置好的ObjectMapper
        return handler;
    }

}
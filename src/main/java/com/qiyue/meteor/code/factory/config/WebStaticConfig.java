package com.qiyue.meteor.code.factory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * created by yebinghuan on 2018/4/22
 *  因为是webpack打包的项目需要手动去配置 静态区域
 */
@Configuration
public class WebStaticConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置模板资源路径
        registry.addResourceHandler("/**").addResourceLocations("classpath:/");
        registry.addResourceHandler("/static").addResourceLocations("classpath:/static");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //默认界面
        registry.addViewController("").setViewName( "forward:/index.html" );
    }
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setRedirectHttp10Compatible(false);
        viewResolver.setContentType("text/html;charset=UTF-8");

        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }

}

package com.spt.engine;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.spt.engine")
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
        configurer.favorParameter(true).ignoreAcceptHeader(true).
                useJaf(false).mediaType("json", MediaType.APPLICATION_PROBLEM_JSON_UTF8);
        //APPLICATION_JSON_UTF8
    }

@Bean
public FilterRegistrationBean filterRegistrationBean() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    characterEncodingFilter.setForceEncoding(true);
    characterEncodingFilter.setEncoding("UTF-8");
    registrationBean.setFilter(characterEncodingFilter);
    return registrationBean;
    }
}

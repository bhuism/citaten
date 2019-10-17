package nl.appsource.stream.demo.config;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

import javax.servlet.Filter;

@Slf4j
@Configuration
@Generated
@EnableWebSecurity
public class CitatenConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    }

    @Bean
    @Profile("production")
    public CommonsRequestLoggingFilter logFilter() {
        final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        return filter;
    }

    @Bean
    @Profile("production")
    public FilterRegistrationBean<Filter> forwardedHeaderFilter() {
        final FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ForwardedHeaderFilter());
        return bean;
    }

}
package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

import javax.servlet.Filter;

@Slf4j
@Configuration
public class DemoConfig {


//    @Bean
//    public EntityLinks citaatEntityLinks() {
//
//        return new EntityLinks() {
//
//            @Override
//            public boolean supports(final Class<?> aClass) {
//                return false;
//            }
//
//            @Override
//            public LinkBuilder linkFor(final Class<?> type) {
//                return null;
//            }
//
//            @Override
//            public LinkBuilder linkFor(final Class<?> type, final Object... parameters) {
//                return null;
//            }
//
//            @Override
//            public LinkBuilder linkForItemResource(final Class<?> type, final Object id) {
//                return null;
//            }
//
//            @Override
//            public Link linkToCollectionResource(final Class<?> type) {
//                return null;
//            }
//
//            @Override
//            public Link linkToItemResource(final Class<?> type, final Object id) {
//                return null;
//            }
//        };
//    }


//    @Bean
//    public LinkRelationProvider citaatlLinkRelationProvider() {
//
//        return new LinkRelationProvider() {
//
//            @Override
//            public LinkRelation getItemResourceRelFor(final Class<?> type) {
//                return LinkRelation.of("aaa");
//            }
//
//            @Override
//            public LinkRelation getCollectionResourceRelFor(final Class<?> type) {
//                return LinkRelation.of("bbb");
//            }
//
//            @Override
//            public boolean supports(final LookupContext delimiter) {
//                return Citaat.class.equals(delimiter.getType());
//            }
//        };
//
//    }

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

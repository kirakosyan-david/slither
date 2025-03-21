package am.example.slither.config;

import org.apache.wicket.protocol.http.WicketFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WicketConfig {

    @Bean
    public FilterRegistrationBean<WicketFilter> wicketFilter(WicketApplication wicketApplication) {
        FilterRegistrationBean<WicketFilter> registration = new FilterRegistrationBean<>();
        WicketFilter filter = new WicketFilter(wicketApplication);
        filter.setFilterPath("/");
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("WicketApplication");
        registration.setOrder(1);
        return registration;
    }

}

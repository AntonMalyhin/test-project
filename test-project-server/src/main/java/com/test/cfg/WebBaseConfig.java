package com.test.cfg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

//@EnableSpringDataWebSupport
@Configuration
public class WebBaseConfig extends DelegatingWebMvcConfiguration {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WebMvcProperties mvcProperties;
    @Autowired
    private ResourceProperties resourceProperties;
    @Autowired
    private ObjectMapper objectMapper;
//    @Autowired
//    private EntityManager entityManager;
    @Autowired
    private SmartValidator validator;

    @Override
    protected Validator getValidator() {
        return validator;
    }

//    @Override
//    protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
//        exceptionResolvers.add((exceptionResolvers.isEmpty() ? 0 : exceptionResolvers.size() - 1), new AuthenticationExceptionResolver());
//    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream().
            filter(c -> (c instanceof MappingJackson2HttpMessageConverter)).
            map(c -> (MappingJackson2HttpMessageConverter) c).
            findFirst().ifPresent(c -> {
            c.setObjectMapper(objectMapper);
        });
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "index.html").setKeepQueryParams(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Duration cachePeriodDur = this.resourceProperties.getCache().getPeriod();
        Integer cachePeriod = (cachePeriodDur != null) ? (int) cachePeriodDur.getSeconds() : null;

        CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();

        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(cachePeriod)
                .setCacheControl(cacheControl);
        }

        String staticPathPattern = this.mvcProperties.getStaticPathPattern();
        if (!registry.hasMappingForPattern(staticPathPattern)) {
            registry.addResourceHandler(staticPathPattern)
                .addResourceLocations(this.resourceProperties.getStaticLocations())
                .setCachePeriod(cachePeriod)
                .setCacheControl(cacheControl);
        }

        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
            .setCachePeriod(cachePeriod)
            .setCacheControl(cacheControl);

    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(searchFilterHandlerMethodArgumentResolver());
//        super.addArgumentResolvers(argumentResolvers);
//
//    }

//    @Bean()
//    public SearchFilterHandlerMethodArgumentResolver searchFilterHandlerMethodArgumentResolver() {
//        return new SearchFilterHandlerMethodArgumentResolver(entityManager);
//    }

//    @Bean
//    public MultipartResolver multipartResolver() {
//        return new PutAwareMultipartResolver();
//    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        return filter;
    }

    @Bean
    public LocaleResolver localeResolver() {

        Locale locale = this.mvcProperties.getLocale();

        LocaleContextHolder.setDefaultLocale(locale);

        if (this.mvcProperties.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
            FixedLocaleResolver localeResolver = new FixedLocaleResolver(locale);
            return localeResolver;
        } else {
            AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
            localeResolver.setDefaultLocale(locale);
            return localeResolver;
        }
    }

//    @Bean
//    public ErrorAttributes errorAttributes() {
//        ErrorMessage bean = new ErrorMessage(messageSource);
//        return bean;
//    }

}

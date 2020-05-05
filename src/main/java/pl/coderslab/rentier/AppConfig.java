package pl.coderslab.rentier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.coderslab.rentier.converter.*;
import pl.coderslab.rentier.utils.BCrypt;
import pl.coderslab.rentier.utils.EmailUtil;

@Configuration
@ComponentScan(basePackages = "pl.coderslab.rentier")
@EnableTransactionManagement
public class AppConfig implements WebMvcConfigurer {

    @Value("${rentier.dataSource}")
    private String dataSource;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if (dataSource.equals("LOCAL")) {
            registry
                    .addResourceHandler("/brands/**")
                    .addResourceLocations("file:///C:\\opt\\rentier\\brands\\");

            registry
                    .addResourceHandler("/products/**")
                    .addResourceLocations("file:///C:\\opt\\rentier\\products\\");

        }

    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(getBrandConverter());
        registry.addConverter(getDeliveryMethodConverter());
        registry.addConverter(getOrderStatusConverter());
        registry.addConverter(getOrderTypeConverter());
        registry.addConverter(getPaymentMethodConverter());
        registry.addConverter(getProductCategoryConverter());
        registry.addConverter(getProductSizeConverter());
        registry.addConverter(getUserRoleConverter());
    }

    @Bean
    public BrandConverter getBrandConverter() {

        return new BrandConverter();
    }

    @Bean
    public DeliveryMethodConverter getDeliveryMethodConverter() {

        return new DeliveryMethodConverter();
    }

    @Bean
    public OrderStatusConverter getOrderStatusConverter() {

        return new OrderStatusConverter();
    }

    @Bean
    public OrderTypeConverter getOrderTypeConverter() {

        return new OrderTypeConverter();
    }

    @Bean
    public PaymentMethodConverter getPaymentMethodConverter() {

        return new PaymentMethodConverter();
    }

    @Bean
    public ProductCategoryConverter getProductCategoryConverter() {

        return new ProductCategoryConverter();
    }

    @Bean
    public ProductSizeConverter getProductSizeConverter() {

        return new ProductSizeConverter();
    }

    @Bean
    public UserRoleConverter getUserRoleConverter() {

        return new UserRoleConverter();
    }

    @Bean
    public BCrypt passwordEncoderOther() {

        return new BCrypt();

    }

    @Bean
    public EmailUtil emailUtil() {

        return new EmailUtil();

    }


//    @Bean
//    public FilterRegistrationBean<adminURLFilter> adminFilterRegistrationBean() {
//        FilterRegistrationBean<adminURLFilter> registrationBean = new FilterRegistrationBean();
//        adminURLFilter customURLFilter = new adminURLFilter();
//
//        registrationBean.setFilter(customURLFilter);
//        registrationBean.addUrlPatterns("/admin/*");
//        registrationBean.setOrder(2); //set precedence
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<userURLFilter> userFilterRegistrationBean() {
//        FilterRegistrationBean<userURLFilter> registrationBean = new FilterRegistrationBean();
//        userURLFilter customURLFilter = new userURLFilter();
//
//        registrationBean.setFilter(customURLFilter);
//        registrationBean.addUrlPatterns("/user/*");
//        registrationBean.setOrder(2); //set precedence
//        return registrationBean;
//    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    webServerFactoryCustomizer() {
        return factory -> factory.setContextPath("/rentier");
    }

//    @Bean
//    public LocalEntityManagerFactoryBean entityManagerFactory() {
//        LocalEntityManagerFactoryBean entityManagerFactoryBean = new LocalEntityManagerFactoryBean();
//        entityManagerFactoryBean.setPersistenceUnitName("rentierPersistenceUnit");
//        return entityManagerFactoryBean;
//    }
//
//    @Bean
//    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
//        return jpaTransactionManager;
//    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean factory =
//                new LocalContainerEntityManagerFactoryBean();
//        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
//        return factory;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory);
//        return txManager;
//    }

    @Bean(name = "multipartResolver ")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1148576);
        return multipartResolver;
    }


//    @Bean
//    public Validator validator() {
//        return new LocalValidatorFactoryBean();
//
//    }
//
//    @Bean(name="localeResolver")
//    public LocaleContextResolver getLocaleContextResolver() {
//        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
//        localeResolver.setDefaultLocale(new Locale("pl","PL"));
//        return localeResolver;
//    }

}

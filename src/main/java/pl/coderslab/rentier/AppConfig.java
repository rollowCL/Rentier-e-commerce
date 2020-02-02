package pl.coderslab.rentier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.coderslab.rentier.converter.*;


@Configuration
@ComponentScan(basePackages = "pl.coderslab.rentier")
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry)
    {
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
    public BCrypt passwordEncoder() {
        return new BCrypt();
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

package com.miro.widrest.config;

import com.miro.widrest.validation.WidgetSizeValidation;
import com.miro.widrest.validation.WidgetValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ValidationConfig {

    @Bean
    public WidgetValidation validation() {
        return new WidgetSizeValidation();
    }
}

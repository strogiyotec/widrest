package com.miro.widrest.config;

import com.miro.widrest.validation.CreateWidgetValidation;
import com.miro.widrest.validation.UpdateWidgetValidation;
import com.miro.widrest.validation.WidgetValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Validation config.
 */
@Configuration(proxyBeanMethods = false)
public class ValidationConfig {

    @Bean
    public Map<WidgetValidation.Type, WidgetValidation> validators() {
        final Map<WidgetValidation.Type, WidgetValidation> map = new HashMap<>(8);
        final WidgetValidation create = new CreateWidgetValidation();
        final WidgetValidation update = new UpdateWidgetValidation(create);
        map.put(WidgetValidation.Type.INSERT, create);
        map.put(WidgetValidation.Type.UPDATE, update);

        return map;
    }
}

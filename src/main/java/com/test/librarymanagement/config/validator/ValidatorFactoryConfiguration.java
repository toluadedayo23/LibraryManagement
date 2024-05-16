package com.test.librarymanagement.config.validator;

import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class ValidatorFactoryConfiguration {
    @Bean("LibraryManagementLocalValidatorFactoryBean")
    public ValidatorFactory LocalValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(
            @Autowired @Qualifier("LibraryManagementLocalValidatorFactoryBean") ValidatorFactory localValidatorFactoryBean) {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidatorFactory(localValidatorFactoryBean);
        return methodValidationPostProcessor;
    }
}

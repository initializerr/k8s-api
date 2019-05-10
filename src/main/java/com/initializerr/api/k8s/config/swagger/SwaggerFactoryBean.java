package com.initializerr.api.k8s.config.swagger;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDateTime;

public class SwaggerFactoryBean implements FactoryBean<Docket>, InitializingBean {
    private Docket docket;
    private String restInterfacePackage;
    private Predicate<String> restInterfacePaths;

    void setRestInterfacePackage(String restInterfacePackage) {
        this.restInterfacePackage = restInterfacePackage;
    }

    void setRestInterfacePaths(Predicate<String> restInterfacePaths) {
        this.restInterfacePaths = restInterfacePaths;
    }

    @Override
    public Docket getObject() throws Exception {
        return docket;
    }

    @Override
    public Class<?> getObjectType() {
        return Docket.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .useDefaultResponseMessages(false)
                .directModelSubstitute(LocalDateTime.class, String.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(restInterfacePackage))
                .build();
    }

    private ApiInfo getApiInfo() {
        return ApiInfo.DEFAULT;
    }
}

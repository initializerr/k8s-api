package com.initializerr.api.k8s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class K8SApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8SApiApplication.class, args);
    }
}


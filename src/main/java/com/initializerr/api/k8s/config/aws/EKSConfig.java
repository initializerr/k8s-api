package com.initializerr.api.k8s.config.aws;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EKSConfig {
    @Value("${eks.cluster.name}")
    String eksClusterName;

    @Value("${eks.cluster.endpoint}")
    String eksClusterEndpoint;

    @Value("${eks.cluster.ca}")
    String eksClusterCa;

    @Bean
    public KubernetesClient kubernetesClient() {
        return EKSClientBuilder.newClientBuilder()
                .withClusterName(eksClusterName)
                .withUrl(eksClusterEndpoint)
                .withCaCert(eksClusterCa)
                .build();
    }
}

package com.initializerr.api.k8s.service.eks;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.DeploymentList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EKSService {

    @Autowired
    KubernetesClient kubernetesClient;

    public ServiceList getServices() {
        ServiceList myNsServices = kubernetesClient.services().inNamespace("default").list();
        return myNsServices;
    }

    public PodList getPods() {
        PodList podList = kubernetesClient.pods().inNamespace("default").list();
        return podList;
    }

    public DeploymentList getDeployments() {
        DeploymentList deploymentList = kubernetesClient.extensions().deployments().inNamespace("default").list();
        return deploymentList;
    }

    public NamespaceList getNamespaces() {
        NamespaceList namespaceList = kubernetesClient.namespaces().list();
        return namespaceList;
    }

    public Pod getPod(String name) {
        Pod pod = kubernetesClient.pods().inNamespace("default").withName(name).get();
        return pod;
    }

    public PersistentVolume getPv(String name){
        PersistentVolume persistentVolume = kubernetesClient.persistentVolumes().withName(name).get();
        return persistentVolume;
    }

    public PersistentVolumeClaim getPvc(String name){
        PersistentVolumeClaim pvc = kubernetesClient.persistentVolumeClaims().inNamespace("default").withName(name).get();
        return pvc;
    }

    public Job getJob(String name) {
        Job job = kubernetesClient.extensions().jobs().inNamespace("default").withName(name).get();
        return job;
    }

}

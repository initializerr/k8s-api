package com.initializerr.api.k8s.controller.eks;

import com.initializerr.api.k8s.service.eks.EKSService;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.DeploymentList;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/eks")
@RequiredArgsConstructor
public class EKSController {

    private final EKSService eksService;

    @RequestMapping(value="/getServices", method= RequestMethod.GET)
    public ServiceList getServices(){
        return eksService.getServices();
    }

    @RequestMapping(value="/getPods", method= RequestMethod.GET)
    public PodList getPods(){
        return eksService.getPods();
    }

    @RequestMapping(value="/getDeployments", method= RequestMethod.GET)
    public DeploymentList getDeployments(){
        return eksService.getDeployments();
    }

    @RequestMapping(value="/getNamespaces", method= RequestMethod.GET)
    public NamespaceList getNamespaces(){
        return eksService.getNamespaces();
    }

    @RequestMapping(value="/getPod", method= RequestMethod.GET)
    public Pod getPod(String name){
        return eksService.getPod(name);
    }

    @RequestMapping(value="/getPersistentVolume", method= RequestMethod.GET)
    public PersistentVolume getPersistentVolume(String name){
        return eksService.getPv(name);
    }

    @RequestMapping(value="/getJob", method= RequestMethod.GET)
    public Job getJob(String name){
        return eksService.getJob(name);
    }

    @RequestMapping(value="/getPersistentVolumeClaim", method= RequestMethod.GET)
    public PersistentVolumeClaim getPersistentVolumeClaim(String name){
        return eksService.getPvc(name);
    }
}

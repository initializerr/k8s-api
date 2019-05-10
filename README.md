# Amazon EKS - KubernetesAPI(Spring Boot REST API io.fabric8.kubernetes java client)


update to your amazon EKS information in application.yaml
```bash
eks:
  cluster:
    name: {AMAZON EKS Cluster name}
    endpoint: {AMAZON EKS API server endpoint}
    ca: {AMAZON EKS Certificate authority}  
```

#### Test from Swagger
```bash
http://localhost:18080/swagger-ui.html
```


#### getPod
```bash
http://localhost:18080/api/v1/eks/getPods
```

#### getServices
```bash
http://localhost:18080/api/v1/eks/getServices
```


---
you can implements more features from
```
https://github.com/fabric8io/kubernetes-client
```
Fabric8 Kubernetes Java client maintained by RedHat.

---
### Environment

- Spring Boot 2.1.3
- Java 1.8
- io.fabric8:kubernetes-client 3.0.0
- com.amazonaws:aws-java-sdk-eks 1.11.483


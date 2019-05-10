# Amazon EKS - Spring Boot REST API(Using io.fabric8 library)

need to change application.yml

```bash
eks:
  cluster:
    name: {AMAZON EKS Cluster name}
    endpoint: {AMAZON EKS API server endpoint}
    ca: {AMAZON EKS Certificate authority}  
```

# Test from Swagger
```bash
http://localhost:18080/swagger-ui.html
```


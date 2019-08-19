package net.oofa.k8sdemo;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CreateController {

    KubernetesClient k8sClient;

    @Autowired
    public CreateController(KubernetesClient kc) {
        k8sClient = kc;
    }

    @PostMapping(name = "/ns", produces = "application/json" )
    public Namespace createRequestNamespace(@RequestParam String nsname) {
        Namespace myns =
                k8sClient
                        .namespaces()
                        .createNew()
                        .withNewMetadata()
                        .withName(nsname)
                        .endMetadata()
                        .done();
        return myns;
    }
//
//    @PostMapping(name = "/namespace/{nsname}", produces = "application/json" )
//    public Namespace createNamespace(@PathVariable String nsname) {
//        Namespace myns =
//                k8sClient
//                        .namespaces()
//                        .createNew()
//                        .withNewMetadata()
//                        .withName(nsname)
//                        .endMetadata()
//                        .done();
//        return myns;
//    }

    @PostMapping(name = "/nginx", consumes = "application/json", produces = "application/json")
    public Deployment createNginx(@RequestBody Map<String, Object> parms) {

        String appname = parms.containsKey("appname") ? parms.get("appname").toString() : "nginx";
        String imagename = parms.containsKey("imagename") ? parms.get("imagename").toString() : "nginx:1.7.9";
        Integer port = parms.containsKey("port") ? Integer.valueOf(parms.get("port").toString()) : 80;

        Deployment deployment =
                k8sClient.apps().deployments().createNew()
                        .withApiVersion("apps/v1")
                        .withNewMetadata()
                            .withName(appname+"-development")
                            .addToLabels("app", appname)
                        .endMetadata()
                        .editOrNewSpec()
                            .withReplicas(3)
                            .withNewSelector()
                                .addToMatchLabels("app", appname)
                            .endSelector()
                            .editOrNewTemplate()
                                .editOrNewMetadata()
                                    .addToLabels("app", appname)
                                .endMetadata()
                                .editOrNewSpec()
                                    .addNewContainer()
                                        .withName(appname)
                                        .withImage(imagename)
                                        .addNewPort()
                                            .withContainerPort(port)
                                        .endPort()
                                    .endContainer()
                                .endSpec()
                            .endTemplate()
                        .endSpec()
                        .done();
        return deployment;
    }



        /*
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  labels:
    app: nginx
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.7.9
        ports:
        - containerPort: 80
         */

}

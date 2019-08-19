package net.oofa.k8sdemo;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class K8sconfig {

    @Bean
    public KubernetesClient config() {
        Config config = new ConfigBuilder().withMasterUrl("https://192.168.70.42:6443").build();
        config.setTrustCerts(true);
        config.setDisableHostnameVerification(true);
        config.setNamespace("default");
        KubernetesClient client = new DefaultKubernetesClient(config);
        Object x = client.apps().deployments().list();
        return client;
    }
}

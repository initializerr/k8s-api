package com.initializerr.api.k8s.config.aws;

import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.HttpMethodName;
import com.google.common.io.BaseEncoding;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.zjsonpatch.internal.guava.Preconditions;
import io.fabric8.zjsonpatch.internal.guava.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URLEncoder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EKSClientBuilder {
    static Logger logger = LoggerFactory.getLogger(EKSConfig.class);

    static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static String BASE64_PADDING = "=";

    static class Entry {
        WeakReference<DefaultKubernetesClient> clientRef;
        String clusterName;
        AWSCredentialsProvider credentialsProvider;
        long refreshSecs = TimeUnit.MINUTES.toSeconds(5);

        protected void refresh() {

            KubernetesClient client = clientRef.get();
            if (client == null) {

                throw new RuntimeException("cancelled refresh");
            }
            logger.info("refreshing auth token for EKS cluster: {}", clusterName);
            try {
                String newToken = generateToken(clusterName, credentialsProvider);

                client.getConfiguration().setOauthToken(newToken);
            } catch (Exception e) {
                logger.warn("problem refreshing", e);
            }
        }
    }


    public static ClientBuilder newClientBuilder() {
        return new ClientBuilder();
    }

    static class ClientBuilder {
        ConfigBuilder configBuilder = new ConfigBuilder();
        String url;
        String clusterName;
        String caCert;
        AWSCredentialsProvider provider = new DefaultAWSCredentialsProviderChain();

        public EKSClientBuilder.ClientBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public EKSClientBuilder.ClientBuilder withClusterName(String name) {
            this.clusterName = name;
            return this;
        }

        public EKSClientBuilder.ClientBuilder withCaCert(String caCert) {
            this.caCert = caCert;
            return this;
        }

        public KubernetesClient build() {

            Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "url must be set");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(clusterName), "clusterName must be set");
            Preconditions.checkArgument(this.provider != null, "credentials provider must be set");
            String initialToken = generateToken(clusterName);
            DefaultKubernetesClient client = new DefaultKubernetesClient(
                    configBuilder.withMasterUrl(url).withCaCertData(caCert).withOauthToken(initialToken).build());

            WeakReference<DefaultKubernetesClient> ref = new WeakReference<DefaultKubernetesClient>(client);

            EKSClientBuilder.Entry entry = new EKSClientBuilder.Entry();
            entry.clientRef = ref;
            entry.credentialsProvider = provider;
            entry.clusterName = clusterName;

            executor.scheduleWithFixedDelay(entry::refresh, 0, entry.refreshSecs, TimeUnit.SECONDS);

            return client;
        }
    }

    public static String generateToken(String clusterName) {
        return generateToken(clusterName, new DefaultAWSCredentialsProviderChain());
    }

    static String generateToken(String clusterName, AWSCredentialsProvider credentials) {
        Request<Void> request = new DefaultRequest<Void>("sts");
        request.setHttpMethod(HttpMethodName.GET);
        request.setEndpoint(URI.create("https://sts.amazonaws.com/"));

        request.addParameter("Action", "GetCallerIdentity");
        request.addParameter("Version", "2011-06-15");
        request.addHeader("x-k8s-aws-id", clusterName);
        AWS4Signer signer = new AWS4Signer();
        signer.setRegionName("us-east-1"); // needs to be us-east-1
        signer.setServiceName("sts");

        signer.presignRequest(request, credentials.getCredentials(),
                new java.util.Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60))); // must be <=60 seconds

        StringBuilder sb = new StringBuilder();
        sb.append("https://sts.amazonaws.com/");

        AtomicInteger count = new AtomicInteger(0);
        request.getParameters().forEach((k, v) -> {
            try {
                sb.append(count.getAndIncrement() == 0 ? "?" : "&");
                sb.append(URLEncoder.encode(k, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(v.get(0).toString(), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
        String encodedUrlWithoutPadding = BaseEncoding.base64Url()
                .encode(sb.toString().getBytes())
                .replaceAll(BASE64_PADDING, "");
        return "k8s-aws-v1." + encodedUrlWithoutPadding;
    }
}

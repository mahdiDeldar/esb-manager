package org.sohagroup.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class OkHttpConfig {
//    @Value("${spring.rabbitmq.listener.simple.max-concurrency}")
    private int maxConcurrency = 10;
//    @Value("${spring.rabbitmq.listener.simple.concurrency}")
    private int concurrency = 10;
    @Bean
    HttpClient httpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000)
            .build();
        return HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig)
            .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .setMaxConnTotal(maxConcurrency)
            .setMaxConnPerRoute(maxConcurrency)
            .build();
    }
}

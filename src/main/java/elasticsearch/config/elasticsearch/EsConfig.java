package elasticsearch.config.elasticsearch;

import elasticsearch.config.exception.BizRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch 연결 설정정보를 생성합니다.
 */
@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "elasticsearch.application.adapter.out.es.indices")
@RequiredArgsConstructor
public class EsConfig {

    // elasticsearch properties 정보
    private final EsProperties esProperties;

    public ClientConfiguration clientConfiguration() {
        final String esUrl = esProperties.elasticsearchUrl();
        final String esUserId = esProperties.getUserId();
        final String esUserPw = esProperties.getPassword();
        final Boolean esUseSsl = esProperties.getUseSsl();
        log.info("set Elasticsearch host : " + esUrl + " UserID : " + esUserId + " useSSL : " + esUseSsl.toString());
        try {
            if (esUseSsl) {
                return ClientConfiguration.builder()
                        .connectedTo(esUrl)
                        .usingSsl(EsSslConfig.getSSLContext(esProperties.getSslCertificateCheck(), esProperties.getSslCert()))
                        .withBasicAuth(esUserId, esUserPw)
                        .withConnectTimeout(10000)
                        .withSocketTimeout(10000)
                        .withClientConfigurer(
                                ElasticsearchClients.ElasticsearchHttpClientConfigurationCallback.from(http -> {
                                    return http.setSSLHostnameVerifier((s, sslSession) -> true);
                                })
                        )
                        .build();
            } else {
                return ClientConfiguration.builder()
                        .connectedTo(esUrl)
                        .withBasicAuth(esUserId, esUserPw)
                        .withConnectTimeout(10000)
                        .withSocketTimeout(10000)
                        .build();
            }
        } catch (Exception e) {
            throw new BizRuntimeException("Elasticsearch Client Configure Error", e);
        }

    }

}

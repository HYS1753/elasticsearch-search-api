package elasticsearch.config.elasticsearch;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * application.yml 에 정의된 Elasticsearch 관련 정보를 파싱합니다.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class EsProperties {

    private String host;                // ES 호스트 정보
    private String port;                // ES 포트 정보
    private String userId;              // ES basic auth ID 정보
    private String password;            // ES basic auth pw 정보
    private Boolean useSsl;                // ES ssl(https) 사용 여부 (default True)
    private Boolean sslCertificateCheck;// ES SSL 인증서 검증 여부 (default True)
    private String sslCert;             // ES SSL PEM Cert text (없을 경우 더미 생성하여 인증)

    public String elasticsearchUrl() {
        return host + ':'
                + ((StringUtils.equalsIgnoreCase("9200", port) ? "" : port));
    }
}

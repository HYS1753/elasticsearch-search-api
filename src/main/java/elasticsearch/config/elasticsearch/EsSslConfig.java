package elasticsearch.config.elasticsearch;

import elasticsearch.config.exception.BizRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Slf4j
public class EsSslConfig {

    private static final String PEMCERTPREFIX = "-----BEGIN CERTIFICATE-----";
    private static final String PEMCERTPOSTFIX = "-----END CERTIFICATE-----";


    /** SSL Context 생성 함수
     */
    public static SSLContext getSSLContext(Boolean sslCertificateCheck, String sslCert) {
        SSLContext sslContext = null;

        if (sslCertificateCheck) {
            sslContext = makeSslContext(validatePemCertString(sslCert));
        } else {
            sslContext = makeDummySslContext();
        }

        return sslContext;
    }

    private static SSLContext makeSslContext(String sslCert) {
        SSLContext sslContext = null;

        try {
            if (StringUtils.isBlank(sslCert)) {
                throw new Exception("Certificate check error, cert info is empty.");
            }
            // 인증서 생성
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(sslCert.getBytes(StandardCharsets.UTF_8)));

            // KeyStore 내 인증서 추가
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry("self_signed_cert", cert);

            // TrustStore 추가
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            log.debug("make user sslContext complete.");
        } catch (Exception e) {
            throw new BizRuntimeException("Elasticsearch get SSL context Error", e);
        }

        return sslContext;
    }

    private static SSLContext makeDummySslContext() {
        SSLContext sslContext = null;

        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true)
                    .build();
            log.debug("make dummy sslContext complete.");
        } catch (Exception e) {
            throw new BizRuntimeException("Elasticsearch get Dummy SSL context Error", e);
        }

        return sslContext;
    }

    private static String validatePemCertString(String pemString) {
        String realPemString = removePemSurrounding(PEMCERTPREFIX, PEMCERTPOSTFIX, pemString);
        String validPemString = realPemString.replaceAll("[\\s\\n]+", "\n");
        return PEMCERTPREFIX + validPemString + PEMCERTPOSTFIX;
    }

    private static String removePemSurrounding(String prefix, String postfix, String pemString) {
        return pemString.replace(prefix, "").replace(postfix, "");
    }
}

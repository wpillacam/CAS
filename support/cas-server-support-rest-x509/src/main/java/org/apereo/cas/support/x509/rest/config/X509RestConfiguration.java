package org.apereo.cas.support.x509.rest.config;

import org.apereo.cas.adaptors.x509.authentication.X509CertificateExtractor;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.rest.factory.RestHttpRequestCredentialFactory;
import org.apereo.cas.rest.plan.RestHttpRequestCredentialFactoryConfigurer;
import org.apereo.cas.support.x509.rest.X509RestHttpRequestHeaderCredentialFactory;
import org.apereo.cas.support.x509.rest.X509RestMultipartBodyCredentialFactory;
import org.apereo.cas.support.x509.rest.X509RestTlsClientCertCredentialFactory;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * X509 Rest configuration class.
 *
 * @author Dmytro Fedonin
 * @since 5.1.0
 */
@Configuration(value = "x509RestConfiguration", proxyBeanMethods = false)
@EnableConfigurationProperties(CasConfigurationProperties.class)
@Slf4j
public class X509RestConfiguration {

    @Bean
    @RefreshScope
    @ConditionalOnMissingBean(name = "x509RestMultipartBody")
    public RestHttpRequestCredentialFactory x509RestMultipartBody() {
        return new X509RestMultipartBodyCredentialFactory();
    }

    @Bean
    @ConditionalOnMissingBean(name = "x509RestRequestHeader")
    @Autowired
    @RefreshScope
    public RestHttpRequestCredentialFactory x509RestRequestHeader(
        @Qualifier("x509CertificateExtractor")
        final X509CertificateExtractor x509CertificateExtractor) {
        return new X509RestHttpRequestHeaderCredentialFactory(x509CertificateExtractor);
    }

    @ConditionalOnProperty(prefix = "cas.rest.x509", name = "tls-client-auth", havingValue = "true")
    @Bean
    @RefreshScope
    public RestHttpRequestCredentialFactory x509RestTlsClientCert() {
        return new X509RestTlsClientCertCredentialFactory();
    }

    @Bean
    @RefreshScope
    @Autowired
    @ConditionalOnMissingBean(name = "x509RestHttpRequestCredentialFactoryConfigurer")
    public RestHttpRequestCredentialFactoryConfigurer x509RestHttpRequestCredentialFactoryConfigurer(
        @Qualifier("x509RestTlsClientCert")
        final RestHttpRequestCredentialFactory x509RestTlsClientCert,
        @Qualifier("x509RestMultipartBody")
        final RestHttpRequestCredentialFactory x509RestMultipartBody,
        @Qualifier("x509RestRequestHeader")
        final RestHttpRequestCredentialFactory x509RestRequestHeader,
        @Qualifier("x509CertificateExtractor")
        final X509CertificateExtractor x509CertificateExtractor,
        final CasConfigurationProperties casProperties) {
        return factory -> {
            val restProperties = casProperties.getRest().getX509();
            val headerAuth = restProperties.isHeaderAuth();
            val bodyAuth = restProperties.isBodyAuth();
            val tlsClientAuth = restProperties.isTlsClientAuth();

            if (tlsClientAuth && (headerAuth || bodyAuth)) {
                LOGGER.warn("The X.509 feature over REST using \"headerAuth\" or \"bodyAuth\" provides a tremendously "
                    + "convenient target for claiming user identities or obtaining TGTs without proof of private "
                    + "key ownership. To securely use this feature, network configuration MUST allow connections "
                    + "to the CAS server only from trusted hosts which in turn have strict security limitations "
                    + "and logging. Thus, \"tlsClientAuth\" shouldn't be activated together with \"headerAuth\" "
                    + "or \"bodyAuth\"");
            }

            if (headerAuth) {
                factory.registerCredentialFactory(x509RestRequestHeader);
            }
            if (bodyAuth) {
                factory.registerCredentialFactory(x509RestMultipartBody);
            }
            if (tlsClientAuth) {
                factory.registerCredentialFactory(x509RestTlsClientCert);
            }
        };
    }

}

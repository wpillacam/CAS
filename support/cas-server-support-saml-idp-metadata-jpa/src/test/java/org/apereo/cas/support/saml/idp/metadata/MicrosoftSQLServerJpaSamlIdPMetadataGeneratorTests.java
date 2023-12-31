package org.apereo.cas.support.saml.idp.metadata;

import org.apereo.cas.util.junit.EnabledIfListeningOnPort;

import org.junit.jupiter.api.Tag;
import org.springframework.test.context.TestPropertySource;

/**
 * This is {@link MicrosoftSQLServerJpaSamlIdPMetadataGeneratorTests}.
 *
 * @author Misagh Moayyed
 * @since 6.0.0
 */
@TestPropertySource(properties = {
    "cas.authn.saml-idp.metadata.jpa.user=sa",
    "cas.authn.saml-idp.metadata.jpa.password=p@ssw0rd",
    "cas.authn.saml-idp.metadata.jpa.driver-class=com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "cas.authn.saml-idp.metadata.jpa.url=jdbc:sqlserver://localhost:1433;databaseName=samlidp;useUnicode=true;characterEncoding=UTF-8;encrypt=false;trustServerCertificate=true",
    "cas.authn.saml-idp.metadata.jpa.dialect=org.hibernate.dialect.SQLServer2016Dialect"
})
@EnabledIfListeningOnPort(port = 1433)
@Tag("MsSqlServer")
class MicrosoftSQLServerJpaSamlIdPMetadataGeneratorTests extends JpaSamlIdPMetadataGeneratorTests {
}

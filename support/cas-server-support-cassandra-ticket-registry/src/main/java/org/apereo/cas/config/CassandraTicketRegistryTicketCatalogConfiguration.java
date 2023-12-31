package org.apereo.cas.config;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.features.CasFeatureModule;
import org.apereo.cas.ticket.catalog.CasTicketCatalogConfigurationValuesProvider;
import org.apereo.cas.util.spring.boot.ConditionalOnFeatureEnabled;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.function.Function;

/**
 * This is {@link CassandraTicketRegistryTicketCatalogConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@EnableConfigurationProperties(CasConfigurationProperties.class)
@ConditionalOnFeatureEnabled(feature = CasFeatureModule.FeatureCatalog.TicketRegistry, module = "cassandra")
@AutoConfiguration
public class CassandraTicketRegistryTicketCatalogConfiguration {

    @ConditionalOnMissingBean(name = "cassandraTicketCatalogConfigurationValuesProvider")
    @Bean
    @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
    public CasTicketCatalogConfigurationValuesProvider cassandraTicketCatalogConfigurationValuesProvider() {
        return new CasTicketCatalogConfigurationValuesProvider() {
            @Override
            public Function<CasConfigurationProperties, String> getServiceTicketStorageName() {
                return p -> "serviceTicketsTable";
            }

            @Override
            public Function<CasConfigurationProperties, String> getProxyTicketStorageName() {
                return p -> "proxyTicketsTable";
            }

            @Override
            public Function<CasConfigurationProperties, String> getTicketGrantingTicketStorageName() {
                return p -> "ticketGrantingTicketsTable";
            }

            @Override
            public Function<CasConfigurationProperties, String> getProxyGrantingTicketStorageName() {
                return p -> "proxyGrantingTicketsTable";
            }

            @Override
            public Function<CasConfigurationProperties, String> getTransientSessionStorageName() {
                return p -> "transientSessionTicketsTable";
            }
        };
    }
}

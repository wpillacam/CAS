package org.apereo.cas.audit.config;

import org.apereo.cas.audit.AuditTrailExecutionPlanConfigurer;
import org.apereo.cas.audit.spi.entity.AuditTrailEntity;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.core.audit.AuditJdbcProperties;
import org.apereo.cas.configuration.model.support.jpa.JpaConfigurationContext;
import org.apereo.cas.configuration.support.JpaBeans;
import org.apereo.cas.jpa.JpaBeanFactory;
import org.apereo.cas.util.CollectionUtils;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.apereo.inspektr.audit.support.JdbcAuditTrailManager;
import org.apereo.inspektr.audit.support.MaxAgeWhereClauseMatchCriteria;
import org.apereo.inspektr.audit.support.WhereClauseMatchCriteria;
import org.apereo.inspektr.common.Cleanable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * This is {@link CasSupportJdbcAuditConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Configuration(value = "casJdbcAuditConfiguration", proxyBeanMethods = false)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties(CasConfigurationProperties.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class CasSupportJdbcAuditConfiguration {
    @Autowired
    @Qualifier("jpaBeanFactory")
    private ObjectProvider<JpaBeanFactory> jpaBeanFactory;

    private static String getAuditTableNameFrom(final AuditJdbcProperties jdbc) {
        var tableName = AuditTrailEntity.AUDIT_TRAIL_TABLE_NAME;
        if (StringUtils.isNotBlank(jdbc.getDefaultSchema())) {
            tableName = jdbc.getDefaultSchema().concat(".").concat(tableName);
        }
        if (StringUtils.isNotBlank(jdbc.getDefaultCatalog())) {
            tableName = jdbc.getDefaultCatalog().concat(".").concat(tableName);
        }
        return tableName;
    }

    @Bean
    @RefreshScope
    @Autowired
    public AuditTrailManager jdbcAuditTrailManager(
        @Qualifier("auditCleanupCriteria")
        final WhereClauseMatchCriteria auditCleanupCriteria,
        @Qualifier("inspektrAuditTransactionTemplate")
        final TransactionTemplate inspektrAuditTransactionTemplate,
        @Qualifier("inspektrAuditTrailDataSource")
        final DataSource inspektrAuditTrailDataSource,
        final CasConfigurationProperties casProperties) {
        val jdbc = casProperties.getAudit().getJdbc();
        val t = new JdbcAuditTrailManager(inspektrAuditTransactionTemplate);
        t.setCleanupCriteria(auditCleanupCriteria);
        t.setDataSource(inspektrAuditTrailDataSource);
        t.setAsynchronous(jdbc.isAsynchronous());
        t.setColumnLength(jdbc.getColumnLength());
        t.setTableName(getAuditTableNameFrom(jdbc));
        if (StringUtils.isNotBlank(jdbc.getSelectSqlQueryTemplate())) {
            t.setSelectByDateSqlTemplate(jdbc.getSelectSqlQueryTemplate());
        }
        if (StringUtils.isNotBlank(jdbc.getDateFormatterPattern())) {
            t.setDateFormatterPattern(jdbc.getDateFormatterPattern());
        }
        return t;
    }

    @ConditionalOnMissingBean(name = "jdbcAuditTrailExecutionPlanConfigurer")
    @Bean
    @RefreshScope
    @Autowired
    public AuditTrailExecutionPlanConfigurer jdbcAuditTrailExecutionPlanConfigurer(
        @Qualifier("jdbcAuditTrailManager")
        final AuditTrailManager jdbcAuditTrailManager) {
        return plan -> plan.registerAuditTrailManager(jdbcAuditTrailManager);
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean inspektrAuditEntityManagerFactory(
        @Qualifier("inspektrAuditTrailDataSource")
        final DataSource inspektrAuditTrailDataSource,
        final CasConfigurationProperties casProperties) {
        val factory = jpaBeanFactory.getObject();
        val ctx = JpaConfigurationContext.builder()
            .jpaVendorAdapter(factory.newJpaVendorAdapter(casProperties.getJdbc()))
            .persistenceUnitName("jpaInspektrAuditContext")
            .dataSource(inspektrAuditTrailDataSource)
            .packagesToScan(CollectionUtils.wrapSet(AuditTrailEntity.class.getPackage().getName()))
            .build();
        return factory.newEntityManagerFactoryBean(ctx, casProperties.getAudit().getJdbc());
    }

    @ConditionalOnMissingBean(name = "auditCleanupCriteria")
    @Bean
    @RefreshScope
    @Autowired
    public WhereClauseMatchCriteria auditCleanupCriteria(final CasConfigurationProperties casProperties) {
        return new MaxAgeWhereClauseMatchCriteria(casProperties.getAudit().getJdbc().getMaxAgeDays());
    }

    @Bean
    @Autowired
    @ConditionalOnMissingBean(name = "inspektrAuditTransactionManager")
    public PlatformTransactionManager inspektrAuditTransactionManager(
        @Qualifier("inspektrAuditTrailDataSource")
        final DataSource inspektrAuditTrailDataSource) {
        return new DataSourceTransactionManager(inspektrAuditTrailDataSource);
    }

    @ConditionalOnMissingBean(name = "inspektrAuditTrailDataSource")
    @Bean
    @RefreshScope
    @Autowired
    public DataSource inspektrAuditTrailDataSource(final CasConfigurationProperties casProperties) {
        return JpaBeans.newDataSource(casProperties.getAudit().getJdbc());
    }

    @ConditionalOnMissingBean(name = "inspektrAuditTransactionTemplate")
    @Bean
    @Autowired
    public TransactionTemplate inspektrAuditTransactionTemplate(
        @Qualifier("inspektrAuditTransactionManager")
        final PlatformTransactionManager inspektrAuditTransactionManager,
        final CasConfigurationProperties casProperties) {
        val t = new TransactionTemplate(inspektrAuditTransactionManager);
        val jdbc = casProperties.getAudit().getJdbc();
        t.setIsolationLevelName(jdbc.getIsolationLevelName());
        t.setPropagationBehaviorName(jdbc.getPropagationBehaviorName());
        return t;
    }

    @ConditionalOnMissingBean(name = "inspektrAuditTrailCleaner")
    @ConditionalOnProperty(prefix = "cas.audit.jdbc.schedule", name = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    @Autowired
    public Cleanable inspektrAuditTrailCleaner(
        @Qualifier("jdbcAuditTrailManager")
        final AuditTrailManager jdbcAuditTrailManager) {
        return new Cleanable() {
            @Scheduled(
                initialDelayString = "${cas.audit.jdbc.schedule.start-delay:10000}",
                fixedDelayString = "${cas.audit.jdbc.schedule.repeat-interval:30000}"
            )
            @Override
            public void clean() {
                jdbcAuditTrailManager.clean();
            }
        };
    }
}

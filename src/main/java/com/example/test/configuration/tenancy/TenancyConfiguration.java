package com.example.test.configuration.tenancy;

import com.example.test.configuration.DataSourcesProperties;
import com.example.test.dto.DataSourceDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA configuration that supports multi-tenancy connection to different data sources based on current tenant.
 *
 * @author s.vareyko
 * @since 23.10.2018
 */
@Configuration
@RequiredArgsConstructor
public class TenancyConfiguration {

    private final JpaProperties jpaProperties;

    @Lazy
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    /**
     * Transaction manager configuration.
     *
     * @param entityManagerFactory - configured entity manager factory
     * @return created transaction manager
     */
    @Lazy
    @Bean
    public JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    /**
     * Default data source that will be used in case of initial scenario where no tenant context is enabled.
     *
     * @return default bean
     */
    @Lazy
    @Primary
    @Bean("defaultDataSource")
    public DataSource defaultDataSourceBean(final DataSourcesProperties properties) {
        final DataSourceDto source = properties.getSource1();
        return DataSourceBuilder.create()
                .driverClassName(source.getDriver())
                .url(source.getUrl())
                .username(source.getUsername())
                .password(source.getPassword())
                .build();
    }

    /**
     * JPA entity manager configuration.
     *
     * @param dataSource         - default data source
     * @param connectionProvider - data source connection provider
     * @param tenantResolver     - tenant key resolver to select data source
     * @param jpaVendorAdapter   - adapter that provide defaults for dialect defaults
     * @return configured entity manager
     * @see CurrentTenantIdentifierResolver
     * @see TenantConnectionProviderImpl
     */
    @Lazy
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final DataSource dataSource,
            final MultiTenantConnectionProvider connectionProvider,
            final CurrentTenantIdentifierResolver tenantResolver,
            final JpaVendorAdapter jpaVendorAdapter
    ) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.example.test.model");
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        final Map<String, Object> hibernateProperties = new HashMap<>();
        // enabling multi-tenancy support configuration
        hibernateProperties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProperties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        hibernateProperties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        //provide naming strategy
        hibernateProperties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class);

        // set configuration from property files
        jpaProperties.getProperties().forEach((key, value) -> setHibernateProperty(hibernateProperties, key));

        entityManagerFactoryBean.setJpaPropertyMap(hibernateProperties);
        return entityManagerFactoryBean;
    }

    private Object setHibernateProperty(final Map<String, Object> hibernateProperties, final String property) {
        return hibernateProperties.put(property, getJpaProperty(property));
    }

    private String getJpaProperty(final String property) {
        return jpaProperties.getProperties().get(property);
    }
}

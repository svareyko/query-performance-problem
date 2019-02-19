package com.example.test.configuration.tenancy;

import com.example.test.configuration.DataSourcesProperties;
import com.example.test.dto.DataSourceDto;
import lombok.AllArgsConstructor;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s.vareyko
 * @since 23.10.2018
 */
@Lazy
@Component
@AllArgsConstructor
public class TenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final DataSource defaultDataSourceBean;
    private final DataSourcesProperties properties;
    /*
     * Map to hold data source for each tenant, key of the map - tenant key, value - data source
     */
    private Map<String, DataSource> dataSourceMap;

    @PostConstruct
    public void load() {
        dataSourceMap = properties.getDataSources().stream()
                .collect(Collectors.toMap(DataSourceDto::getName, source -> DataSourceBuilder.create()
                        .driverClassName(source.getDriver())
                        .url(source.getUrl())
                        .username(source.getUsername())
                        .password(source.getPassword())
                        .build()));
    }

    /**
     * Provides ability to add new data source by tenant key.
     *
     * @param tenantKey        - tenant key
     * @param tenantDatasource - tenant's data source
     */
    public void addDataSource(final String tenantKey, final DataSource tenantDatasource) {
        this.dataSourceMap.put(tenantKey, tenantDatasource);
    }

    /**
     * Provides data source for tenant by tenant key
     * without default value.
     *
     * @param tenantKey - tenant key
     * @return tenant's data source or null
     */
    public DataSource getDataSource(final String tenantKey) {
        return dataSourceMap.get(tenantKey);
    }

    /**
     * Provides default data source.
     *
     * @return default data source
     */
    @Override
    protected DataSource selectAnyDataSource() {
        return defaultDataSourceBean;
    }

    /**
     * Provides data source for tenant by tenant key. In case of no data source found - default data source will
     * be returned
     *
     * @param tenantKey - tenant key
     * @return tenant's data source or default one
     */
    @Override
    protected DataSource selectDataSource(final String tenantKey) {
        return dataSourceMap.getOrDefault(tenantKey, defaultDataSourceBean);
    }

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }
}

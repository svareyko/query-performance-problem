package com.example.test.configuration;

import com.example.test.dto.DataSourceDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author s.vareyko
 * @since 10/22/2018
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("app.datasources")
public class DataSourcesProperties {

    private DataSourceDto source1;
    private DataSourceDto source2;
    private DataSourceDto source3;

    /**
     * Special method for obtaining of iterable data sources.
     *
     * @return collection with data sources
     */
    public Collection<DataSourceDto> getDataSources() {
        return Arrays.asList(source1, source2, source3);
    }


}

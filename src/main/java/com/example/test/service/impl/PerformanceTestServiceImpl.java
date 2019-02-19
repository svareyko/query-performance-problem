package com.example.test.service.impl;

import com.example.test.configuration.DataSourcesProperties;
import com.example.test.configuration.TenantContextHolder;
import com.example.test.dto.DataSourceDto;
import com.example.test.dto.ResultDto;
import com.example.test.model.DataEntity;
import com.example.test.repository.DataRepository;
import com.example.test.service.PerformanceTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author sergei.vareyko
 * @since 22.10.2018
 */
@Service
@RequiredArgsConstructor
public class PerformanceTestServiceImpl implements PerformanceTestService {

    private final DataSourcesProperties dataSources;
    private final DataRepository repository;

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public Collection<ResultDto> run(final String query) throws InterruptedException {
        final Collection<DataSourceDto> dataSources = this.dataSources.getDataSources();
        final Collection<Callable<ResultDto>> tasks = new ArrayList<>(dataSources.size());
        dataSources.forEach(dataSource -> tasks.add(() -> executeQuery(query, dataSource)));

        return threadPool.invokeAll(tasks).stream()
                .map(this::getResult)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Helper method for running query in transaction.
     *
     * @param query      for search
     * @param dataSource for run query
     * @return dto with results
     */
    @Transactional
    ResultDto executeQuery(final String query, final DataSourceDto dataSource) {
        TenantContextHolder.setContext(dataSource.getName());
        final long startTime = System.currentTimeMillis();
        final List<DataEntity> foundData = repository.findAllByTextContains(query);
        final long resultTime = System.currentTimeMillis() - startTime;
        return new ResultDto(dataSource.getName(), resultTime, foundData);
    }

    /**
     * Helper method for obtaining result from future using try-catch block.
     *
     * @param future for processing
     * @return dto with result
     */
    private ResultDto getResult(final Future<ResultDto> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ignore) {
            // we don't need anything here
        }
        return null;
    }
}

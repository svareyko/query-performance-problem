package com.example.test.service;

import com.example.test.dto.ResultDto;

import java.util.Collection;

/**
 * @author s.vareyko
 * @since 10/22/18
 */
public interface PerformanceTestService {

    /**
     * Main method that run performance test.
     *
     * @param query for search
     * @return DTO with results from each data source
     */
    Collection<ResultDto> run(String query) throws InterruptedException;
}

package com.example.test.controller;

import com.example.test.dto.ResultDto;
import com.example.test.service.PerformanceTestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author s.vareyko
 * @since 10/22/2018
 */
@RestController
@AllArgsConstructor
@RequestMapping("/search")
public class PerformanceTestController {

    private final PerformanceTestService service;

    @GetMapping("/{query}")
    public Collection<ResultDto> test(@PathVariable final String query) throws InterruptedException {
        return service.run(query);
    }
}

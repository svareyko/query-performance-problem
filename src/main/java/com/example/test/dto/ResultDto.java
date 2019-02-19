package com.example.test.dto;

import com.example.test.model.DataEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author s.vareyko
 * @since 10/22/2018
 */
@Getter
@Setter
@AllArgsConstructor
public class ResultDto {
    private String source;
    private Long time;
    private List<DataEntity> results;
}

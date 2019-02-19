package com.example.test.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author s.vareyko
 * @since 22.10.2018
 */
@Getter
@Setter
public class DataSourceDto {
    private String driver;
    private String name;
    private String url;
    private String username;
    private String password;
}

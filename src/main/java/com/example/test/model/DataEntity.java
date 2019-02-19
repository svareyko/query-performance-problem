package com.example.test.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author s.vareyko
 * @since 22.10.2018
 */
@Setter
@Getter
@Entity
@Table
public class DataEntity {
    @Id
    private Long id;
    private String text;
}

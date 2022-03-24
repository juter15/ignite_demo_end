package com.example.ignite_demo2;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

@Data
public class test1 implements Serializable {
    @QuerySqlField(index = true)
    private Long id;
    @QuerySqlField(index = true)
    private String name;
}

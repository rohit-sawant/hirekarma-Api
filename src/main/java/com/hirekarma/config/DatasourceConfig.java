package com.hirekarma.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceConfig {
    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create()
          .driverClassName("org.postgresql.Driver")
          .url("jdbc:postgresql://ec2-54-158-98-97.compute-1.amazonaws.com:5432/ddn3h5ktfjbogg")
          .username("vcfpghfckcgcnb")
          .password("61ede4a4e8fd74108d5349a05dde05c34127cb7feb38557ba0c13d201bac1d12")
          .build();	
    }
}
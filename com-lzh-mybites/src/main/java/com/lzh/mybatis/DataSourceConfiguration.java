package com.lzh.mybatis;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin.tan on 2017/8/18.
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {

    @Value("${druid.type}")
    private Class<? extends DataSource> dataSourceType;

    @Bean(name = "winposMasterDataSource")
    @ConfigurationProperties(prefix = "druid.winpos.master")
    @RefreshScope
    public DataSource winposMasterDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "winposSlaveDataSource")
    @ConfigurationProperties(prefix = "druid.winpos.slave")
    @RefreshScope
    public DataSource winposSlaveDataSource1() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "dataSource")
    @Primary
    public AbstractRoutingDataSource dataSource() {
        MasterSlaveRoutingDataSource proxy = new MasterSlaveRoutingDataSource();
        Map<Object, Object> targetDataResources = new HashMap<>();
        targetDataResources.put(DbContextHolder.DbType.WINPOS_MASTER, winposMasterDataSource());
        targetDataResources.put(DbContextHolder.DbType.WINPOS_SLAVE, winposSlaveDataSource1());
        proxy.setDefaultTargetDataSource(winposMasterDataSource());
        proxy.setTargetDataSources(targetDataResources);
        proxy.afterPropertiesSet();
        return proxy;
    }

}
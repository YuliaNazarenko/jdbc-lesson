package com.eurotechstudy.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPoolConfig {
    private static final String URL = "db.url";
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";

    private static HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropertiesUtil.get(URL));
        config.setUsername(PropertiesUtil.get(USERNAME));
        config.setPassword(PropertiesUtil.get(PASSWORD));
        config.setMaximumPoolSize(5);
        config.setIdleTimeout(10*60*1000);
        return config;
    }

    public static HikariDataSource getHikariDataSource() {
        HikariConfig config = getHikariConfig();
        return new HikariDataSource(config);
    }
}


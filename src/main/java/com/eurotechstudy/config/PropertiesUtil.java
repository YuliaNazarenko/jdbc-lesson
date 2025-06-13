package com.eurotechstudy.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final Properties properties = new Properties();


    static {
        loadProperties();
    }

    private  PropertiesUtil(){

    }


    public static String get(String key) {
        return properties.getProperty(key);
    }


    private static void loadProperties() {
        InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
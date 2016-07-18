package com.orange.clara.pivotaltrackermirror.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 15/07/2016
 */
@Configuration
public class AppConfig {
    @Value("#{${security.require_ssl:false} ? 'https://' : 'http://'}")
    private String appProtocol;
    @Value("${vcap.application.uris[0]:localhost:8081}")
    private String appUri;

    @Value("${refresh.mirror.after.minutes:120}")
    private Integer refreshMirrorMinutes;

    @Profile("!dev")
    @Bean(name = "debugTraceController")
    public Boolean noDebugTraceController() {
        return false;
    }

    @Profile("dev")
    @Bean(name = "debugTraceController")
    public Boolean debugTraceController() {
        return true;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public String appUrl() {
        return appProtocol + this.appUri;
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(TimeZone.getDefault());
        builder.dateFormat(df);
        return builder;
    }

    @Bean
    public Integer refreshMirrorMinutes() {
        return refreshMirrorMinutes;
    }
}

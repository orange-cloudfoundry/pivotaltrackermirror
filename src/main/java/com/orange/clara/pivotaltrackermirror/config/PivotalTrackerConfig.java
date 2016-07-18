package com.orange.clara.pivotaltrackermirror.config;

import com.mashape.unirest.http.Unirest;
import com.orange.clara.pivotaltrackermirror.util.ProxyUtil;
import onespot.pivotal.api.PivotalTracker;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 13/07/2016
 */
@Configuration
public class PivotalTrackerConfig {

    @Value("${pivotaltracker.token:#{null}}")
    public String pivotalTrackerToken;

    @Bean
    public PivotalTracker pivotalTrackerClient() {
        HttpHost httpHost = ProxyUtil.getHttpHost();
        if (httpHost != null) {
            Unirest.setProxy(httpHost);
        }
        return new PivotalTracker(pivotalTrackerToken);
    }
}

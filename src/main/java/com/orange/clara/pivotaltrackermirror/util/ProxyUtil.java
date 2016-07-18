package com.orange.clara.pivotaltrackermirror.util;

import org.apache.http.HttpHost;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

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
public class ProxyUtil {
    private static final String PROXY_HOST = System.getProperty("http.proxyHost", null);

    private static final String PROXY_PASSWD = System.getProperty("http.proxyPassword", null);

    private static final int PROXY_PORT = Integer.getInteger("http.proxyPort", 80);

    private static final String PROXY_USER = System.getProperty("http.proxyUsername", null);

    private static boolean authenticatorIsLoaded = false;

    public static Proxy getProxy() throws Exception {
        if (PROXY_HOST == null) {
            return null;
        }
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
        if (PROXY_USER == null) {
            return proxy;
        }
        loadAuthenticator();
        return proxy;
    }

    public static HttpHost getHttpHost() {
        if (PROXY_HOST == null) {
            return null;
        }
        HttpHost httpHost = new HttpHost(PROXY_HOST, PROXY_PORT);
        if (PROXY_USER == null) {
            return httpHost;
        }
        loadAuthenticator();
        return httpHost;
    }

    private static void loadAuthenticator() {
        if (authenticatorIsLoaded) {
            return;
        }
        authenticatorIsLoaded = true;
        Authenticator authenticator = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(PROXY_USER,
                        PROXY_PASSWD.toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);

    }
}

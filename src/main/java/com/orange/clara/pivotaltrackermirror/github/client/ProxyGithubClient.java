package com.orange.clara.pivotaltrackermirror.github.client;

import com.orange.clara.pivotaltrackermirror.util.ProxyUtil;
import org.eclipse.egit.github.core.client.GitHubClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

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
public class ProxyGithubClient extends GitHubClient {

    @Override
    protected HttpURLConnection createConnection(String uri) throws IOException {
        URL url = new URL(createUri(uri));
        Proxy proxy = null;
        try {
            proxy = ProxyUtil.getProxy();
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
        if (proxy != null) {
            return (HttpURLConnection) url.openConnection(proxy);
        }
        return (HttpURLConnection) url.openConnection();
    }


}

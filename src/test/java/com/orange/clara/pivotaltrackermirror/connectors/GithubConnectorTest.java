package com.orange.clara.pivotaltrackermirror.connectors;

import org.junit.Before;

/**
 * Copyright (C) 2016 Arthur Halet
 * <p>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p>
 * Author: Arthur Halet
 * Date: 26/07/2016
 */
public class GithubConnectorTest {
    private GithubConnector githubConnector;

    @Before
    public void setUp() {
        githubConnector = new GithubConnector(null);
    }


}
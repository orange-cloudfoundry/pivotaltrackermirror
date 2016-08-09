package com.orange.clara.pivotaltrackermirror.connectors;

import org.eclipse.egit.github.core.Issue;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Copyright (C) 2016 Arthur Halet
 * <p>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p>
 * Author: Arthur Halet
 * Date: 28/07/2016
 */
public class GithubConnectorTest {
    private GithubConnector githubConnector;

    @Before
    public void setUp() {
        githubConnector = new GithubConnector();
    }

    @Test
    public void getIdFromTitle() {
        String title = "this is a story" + GithubConnector.STORY_ID_KEY + "12";
        Issue issue = new Issue();
        issue.setTitle(title);
        assertThat(githubConnector.getStoryIdFromIssueTitle(issue)).isEqualTo(12);
    }
}
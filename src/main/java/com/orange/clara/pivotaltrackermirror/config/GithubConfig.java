package com.orange.clara.pivotaltrackermirror.config;

import com.orange.clara.pivotaltrackermirror.connectors.GithubConnector;
import com.orange.clara.pivotaltrackermirror.converter.PivotalTrackerToGithubConverter;
import com.orange.clara.pivotaltrackermirror.github.client.ProxyGithubClient;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
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
public class GithubConfig {

    @Value("${github.token:#{null}}")
    public String githubToken;

    @Bean
    public GitHubClient gitHubClient() {
        GitHubClient client = new ProxyGithubClient();
        if (githubToken != null) {
            client.setOAuth2Token(githubToken);
        }
        return client;
    }

    @Bean
    public UserService userService() {
        return new UserService(this.gitHubClient());
    }

    @Bean
    public RepositoryService repositoryService() {
        return new RepositoryService(this.gitHubClient());
    }

    @Bean
    public IssueService issueService() {
        return new IssueService(this.gitHubClient());
    }

    @Bean
    public GithubConnector githubConnector() {
        return new GithubConnector(this.issueService());
    }

    @Bean
    public PivotalTrackerToGithubConverter pivotalTrackerToGithubConverter() {
        return new PivotalTrackerToGithubConverter(this.githubConnector());
    }
}

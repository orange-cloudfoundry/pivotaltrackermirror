package com.orange.clara.pivotaltrackermirror.connectors;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorException;
import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorPostCommentException;
import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorPostStoryException;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import onespot.pivotal.api.resources.Story;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
public class GithubConnector implements Connector<Issue, Comment> {
    protected final static String REPO_PATTERN = "^([^/]+)/([^/]+)$";
    protected final static String STORY_ID_KEY = " - Story Id: ";
    protected final static String COMMENT_ID_KEY = "Comment Id: ";
    protected final static String STORY_ID_PATTERN = ".*" + STORY_ID_KEY + "([0-9]+)$";
    protected final static String COMMENT_ID_PATTERN = "^" + COMMENT_ID_KEY + "([0-9]+)$";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private IssueService issueService;

    public GithubConnector(IssueService issueService) {
        this.issueService = issueService;
    }

    protected RepositoryId getRepositoryId(String target) throws ConnectorException {
        Pattern p = Pattern.compile(REPO_PATTERN);
        Matcher matcher = p.matcher(target);
        if (!matcher.find()) {
            return null;
        }
        String owner = matcher.group(1);
        String repoName = matcher.group(2);
        return new RepositoryId(owner, repoName);
    }

    @Override
    public Issue postOrUpdateStory(MirrorReference mirrorReference, Issue issue) throws ConnectorException {

        RepositoryId repositoryId = this.getRepositoryId(mirrorReference.getTarget());
        try {
            if (mirrorReference.getUpdatedAt() == null) {
                logger.debug("Posting new issue on github with title '{}'.", issue.getTitle());
                this.waitSend();
                return this.issueService.createIssue(repositoryId, issue);
            }
            Integer storyId = this.getStoryIdFromIssueTitle(issue);
            Issue existingIssue = this.findIssue(repositoryId, storyId);
            if (existingIssue == null) {
                logger.debug("Posting new issue on github with title '{}' because no existing was found.", issue.getTitle());
                this.waitSend();
                return this.issueService.createIssue(repositoryId, issue);
            }
            issue.setNumber(existingIssue.getNumber());
            if (issuesEquals(existingIssue, issue)) {
                return issue;
            }
            logger.debug("Editing existing issue on github with title '{}'.", issue.getTitle());
            this.waitSend();
            return this.issueService.editIssue(repositoryId, issue);
        } catch (IOException e) {
            throw new ConnectorPostStoryException(e.getMessage(), e);
        }
    }

    @Override
    public Comment postOrUpdateComment(MirrorReference mirrorReference, Issue issue, Comment comment) throws ConnectorException {
        RepositoryId repositoryId = this.getRepositoryId(mirrorReference.getTarget());
        try {
            if (mirrorReference.getUpdatedAt() == null) {
                logger.debug("Posting new comment on issue '{}' on github", issue.getNumber());
                this.waitSend();
                return this.issueService.createComment(repositoryId, issue.getNumber(), comment.getBody());
            }
            Integer commentId = this.getCommentIdFromCommentGithub(comment);
            Comment existingComment = this.findComment(repositoryId, issue.getNumber(), commentId);
            if (existingComment == null) {
                logger.debug("Posting new comment on issue '{}' on github because no existing was found", issue.getNumber());
                this.waitSend();
                return this.issueService.createComment(repositoryId, issue.getNumber(), comment.getBody());
            }
            comment.setId(existingComment.getId());
            if (existingComment.getBody().equals(comment.getBody())) {
                return comment;
            }
            logger.debug("Editing existing comment on issue '{}' on github", issue.getNumber());
            this.waitSend();
            return this.issueService.editComment(repositoryId, comment);
        } catch (IOException e) {
            throw new ConnectorPostCommentException(e.getMessage(), e);
        }

    }

    @Override
    public Issue convertStory(Story story) {
        Issue issue = new Issue();
        issue.setTitle(story.getName() + STORY_ID_KEY + story.getId());
        if (story.currentState.equals(Story.StoryState.accepted)) {
            issue.setState("closed");
        } else {
            issue.setState("open");
        }
        List<Label> labels = Lists.newArrayList();
        labels.add(this.createLabelForStatus(story.currentState));
        labels.add(new Label().setName(story.storyType.name()));
        issue.setLabels(labels);
        issue.setBody(this.createBodyStory(story));
        return issue;
    }

    @Override
    public Comment convertComment(onespot.pivotal.api.resources.Comment comment) {
        Comment githubComment = new Comment();
        githubComment.setBody(this.createBodyComment(comment));
        return githubComment;
    }

    protected void waitSend() throws ConnectorException {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new ConnectorException(e.getMessage(), e);
        }
    }

    private Label createLabelForStatus(Story.StoryState storyState) {
        Label label = new Label();
        label.setName(storyState.name().toLowerCase());
        switch (storyState) {
            case accepted:
                label.setColor("4e8200");
                break;
            case delivered:
                label.setColor("f08000");
                break;
            case finished:
                label.setColor("172c51");
                break;
            case planned:
                label.setColor("e0c85e");
                break;
            case rejected:
                label.setColor("950828");
                break;
            case started:
                label.setColor("c8cbd0");
                break;
            case unscheduled:
                label.setColor("c8cbd0");
                break;
            case unstarted:
                label.setColor("e0c85e");
                break;
        }
        return label;
    }

    public boolean issuesEquals(Issue issue1, Issue issue2) {

        return issue1.getLabels().equals(issue2.getLabels())
                && issue1.getTitle().equals(issue2.getTitle())
                && issue1.getBody().equals(issue2.getBody())
                && issue1.getState().equals(issue2.getState());

    }

    protected int getStoryIdFromIssueTitle(Issue issue) {
        Pattern p = Pattern.compile(STORY_ID_PATTERN);
        Matcher matcher = p.matcher(issue.getTitle());
        if (!matcher.find()) {
            return 0;
        }
        return Integer.valueOf(matcher.group(1));
    }

    protected int getCommentIdFromCommentGithub(Comment comment) {
        String[] bodySpliced = comment.getBody().split("\n");
        String idString = bodySpliced[bodySpliced.length - 1];
        Pattern p = Pattern.compile(COMMENT_ID_PATTERN);
        Matcher matcher = p.matcher(idString);
        if (!matcher.find()) {
            return 0;
        }
        return Integer.valueOf(matcher.group(1));
    }

    protected Comment findComment(RepositoryId repositoryId, int issueId, int commentId) throws IOException {
        if (commentId <= 0) {
            return null;
        }
        List<Comment> comments = this.issueService.getComments(repositoryId, issueId);
        for (Comment comment : comments) {
            if (this.getCommentIdFromCommentGithub(comment) == commentId) {
                return comment;
            }
        }
        return null;
    }

    protected Issue findIssue(RepositoryId repositoryId, int storyId) throws IOException {

        if (storyId <= 0) {
            return null;
        }
        List<SearchIssue> searchIssues = this.issueService.searchIssues(repositoryId, "all", STORY_ID_KEY + storyId + " in:title");
        if (searchIssues.isEmpty()) {
            return null;
        }
        SearchIssue searchIssue = searchIssues.get(0);
        Issue issue = new Issue();
        issue.setTitle(searchIssue.getTitle());
        issue.setBody(searchIssue.getBody());
        List<Label> labels = searchIssue.getLabels().stream().map(labelName -> new Label().setName(labelName)).collect(Collectors.toList());
        issue.setLabels(labels);
        issue.setNumber(searchIssue.getNumber());
        return issue;
    }

    private List<Label> convertLabel(List<onespot.pivotal.api.resources.Label> labelsPivotal) {
        List<Label> labels = Lists.newArrayList();
        if (labelsPivotal == null) {
            return labels;
        }
        for (onespot.pivotal.api.resources.Label labelPivotal : labelsPivotal) {
            Label labelGithub = new Label();
            labelGithub.setName(labelPivotal.name);
            labels.add(labelGithub);
        }
        return labels;
    }

    protected String createBodyComment(onespot.pivotal.api.resources.Comment comment) {
        List<String> commentBody = Lists.newArrayList();
        commentBody.add("- **Link**: " + comment.getUrl());
        if (comment.getPerson() != null) {
            commentBody.add("- **User**: " + comment.getPerson().name);
        }

        commentBody.add("\n" + this.sanitizer(comment.getText()) + "\n");
        commentBody.add(COMMENT_ID_KEY + comment.getId());
        return Joiner.on("\n").join(commentBody);
    }

    protected String createBodyStory(Story story) {
        List<String> storyBody = Lists.newArrayList();
        storyBody.add("- **Link**: " + story.getUrl());
        if (story.getLabels() != null) {
            storyBody.add("- **Labels**: " + Joiner.on(", ").join(this.convertLabel(story.getLabels()).stream().map(Label::getName).collect(Collectors.toList())));
        }
        if (story.requester != null && story.requester.name != null) {
            storyBody.add("- **Requester**: " + story.requester.name);
        }

        if (story.getOwners() != null && !story.getOwners().isEmpty()) {
            storyBody.add("- **Owners**: " + Joiner.on(", ").join(story.getOwners().stream().map(p -> p.name).collect(Collectors.toList())));
        }
        if (story.estimate >= 0) {
            storyBody.add("- **Estimate**: " + story.estimate);
        }

        storyBody.add("\n" + this.sanitizer(story.description));
        return Joiner.on("\n").join(storyBody);
    }

    private String sanitizer(String text) {
        return text.replace(" @", "");
    }

    public IssueService getIssueService() {
        return issueService;
    }

    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }
}

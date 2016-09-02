package com.orange.clara.pivotaltrackermirror.connectors;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorException;
import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorPostCommentException;
import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorPostStoryException;
import com.orange.clara.pivotaltrackermirror.github.client.ProxyGithubClient;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.util.MarkdownSanitizer;
import onespot.pivotal.api.resources.Story;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.LabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
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
    protected final static String DATE_SHORT_PATTERN = "MMM d, yyyy z";
    protected final static String DATE_LONG_PATTERN = "MMM d, yyyy, h.mm a z";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private IssueService issueService;
    private LabelService labelService;

    public GithubConnector() {
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
            this.createLabelsOnGithub(repositoryId, issue.getLabels());
            if (mirrorReference.getUpdatedAt() == null) {
                logger.debug("Posting new issue on github with title '{}'.", issue.getTitle());
                return createIssue(repositoryId, issue);
            }
            Integer storyId = this.getStoryIdFromIssueTitle(issue);
            Issue existingIssue = this.findIssue(repositoryId, storyId);
            if (existingIssue == null) {
                logger.debug("Posting new issue on github with title '{}' because no existing was found.", issue.getTitle());
                return createIssue(repositoryId, issue);
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
        labels.add(new Label().setName(story.storyType.name()).setColor("ededed"));
        if (story.getLabels() != null) {
            labels.addAll(this.convertLabel(story.getLabels()));
        }

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

    @Override
    public void loadClient(String token) {
        if (issueService != null) {
            return;
        }
        GitHubClient client = new ProxyGithubClient();
        if (token != null) {
            client.setOAuth2Token(token);
        }
        this.issueService = new IssueService(client);
        this.labelService = new LabelService(client);
    }

    private Issue createIssue(RepositoryId repositoryId, Issue originalIssue) throws IOException, ConnectorException {
        this.waitSend();
        Issue issue = this.issueService.createIssue(repositoryId, originalIssue);
        // github api doesnt allow to set the issue state during creation
        if (originalIssue.getState().equals("closed")) {
            this.waitSend();
            issue = this.issueService.editIssue(repositoryId, originalIssue);
        }
        return issue;
    }

    private void createLabelsOnGithub(RepositoryId repositoryId, List<Label> labels) throws IOException {
        List<Label> existingLabels = this.labelService.getLabels(repositoryId);
        for (Label label : labels) {
            if (existingLabels.contains(label)) {
                continue;
            }
            this.labelService.createLabel(repositoryId, label);
        }

    }

    protected void waitSend() throws ConnectorException {
        try {
            Random randomGenerator = new Random();
            Thread.sleep(2000L + (long) (randomGenerator.nextInt(6)) * 1000L);
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
            default:
                label.setColor("ededed");
                break;
        }
        return label;
    }

    private boolean issuesEquals(Issue issue1, Issue issue2) {

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
        String idString = bodySpliced[bodySpliced.length - 2];
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
            labelGithub.setColor("ededed");
            labels.add(labelGithub);
        }
        return labels;
    }

    protected String createBodyComment(onespot.pivotal.api.resources.Comment comment) {
        List<String> commentBody = Lists.newArrayList();
        String personName = "*Anonymous*";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_LONG_PATTERN);
        if (comment.getPerson() != null) {
            personName = comment.getPerson().name;
        }
        commentBody.add(String.format("%s [commented](%s) on %s:", personName, comment.getUrl(), dateFormat.format(Date.from(comment.updatedAt))));
        commentBody.add("\n" + MarkdownSanitizer.sanitize(comment.getText()) + "\n");
        commentBody.add("<!--\n" + COMMENT_ID_KEY + comment.getId() + "\n-->");
        return Joiner.on("\n").join(commentBody);
    }

    protected String createBodyStory(Story story) {
        List<String> storyBody = Lists.newArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_SHORT_PATTERN);

        storyBody.add(MarkdownSanitizer.sanitize(story.description) + "\n");

        storyBody.add("---");

        storyBody.add(String.format("\nMirrors: [story %s](%s) submitted on %s", story.id, story.getUrl(), dateFormat.format(Date.from(story.getCreatedAt()))));
        if (story.requester != null && story.requester.name != null) {
            storyBody.add("- **Requester**: " + story.requester.name);
        }

        if (story.getOwners() != null && !story.getOwners().isEmpty()) {
            storyBody.add("- **Owners**: " + Joiner.on(", ").join(story.getOwners().stream().map(p -> p.name).collect(Collectors.toList())));
        }
        if (story.estimate >= 0) {
            storyBody.add("- **Estimate**: " + story.estimate);
        }
        return Joiner.on("\n").join(storyBody);
    }


    public IssueService getIssueService() {
        return issueService;
    }

    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }
}

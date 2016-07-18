package com.orange.clara.pivotaltrackermirror.model;

import com.google.common.collect.Lists;
import onespot.pivotal.api.resources.Comment;
import onespot.pivotal.api.resources.Story;

import java.util.List;

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
public class StoryCompleteReference extends Story {

    private List<Comment> comments;

    public StoryCompleteReference(Story story) {
        comments = Lists.newArrayList();
        this.convertStory(story);
    }

    public StoryCompleteReference(Story story, List<Comment> comments) {
        this.convertStory(story);
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public StoryCompleteReference setComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    private void convertStory(Story story) {
        this.id = story.id;
        this.projectId = story.projectId;
        this.name = story.name;
        this.requestedById = story.requestedById;
        this.requester = story.requester;
        this.kind = story.kind;
        this.ownerIds = story.ownerIds;
        this.labelIds = story.labelIds;
        this.taskIds = story.taskIds;
        this.followerIds = story.followerIds;
        this.owners = story.owners;
        this.labels = story.labels;
        this.integrationId = story.integrationId;
        this.url = story.url;
        this.storyType = story.storyType;
        this.acceptedAt = story.acceptedAt;
        this.afterId = story.afterId;
        this.beforeId = story.beforeId;
        this.commentIds = story.commentIds;
        this.createdAt = story.createdAt;
        this.currentState = story.currentState;
        this.deadline = story.deadline;
        this.description = story.description;
        this.estimate = story.estimate;
        this.acceptedAt = story.acceptedAt;
        this.externalId = story.externalId;
        this.followerIds = story.followerIds;

    }

    @Override
    public String toString() {
        return super.toString() + "\nStoryCompleteReference{" +
                "comments=" + comments +
                '}';
    }
}

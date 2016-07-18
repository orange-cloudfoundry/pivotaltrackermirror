package com.orange.clara.pivotaltrackermirror.converter;


import com.orange.clara.pivotaltrackermirror.connectors.Connector;
import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorException;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;

import java.util.List;

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
public class PivotalTrackerToGithubConverter extends AbstractPivotalTrackerConverter<Issue, Comment> {

    public PivotalTrackerToGithubConverter(Connector<Issue, Comment> connector) {
        super(connector);
    }


    @Override
    protected Issue sendStory(MirrorReference mirrorReference, Issue convertedStory) throws ConnectorException {
        Issue issue = super.sendStory(mirrorReference, convertedStory);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new ConnectorException(e.getMessage(), e);
        }
        return issue;
    }

    @Override
    protected void sendComments(MirrorReference mirrorReference, Issue story, List<onespot.pivotal.api.resources.Comment> comments) throws ConnectorException {
        super.sendComments(mirrorReference, story, comments);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new ConnectorException(e.getMessage(), e);
        }
    }

    @Override
    public String createLink(MirrorReference mirrorReference) {
        return "https://github.com/" + mirrorReference.getTarget() + "/issues";
    }
}

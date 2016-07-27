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
    public String createLink(MirrorReference mirrorReference) {
        return "https://github.com/" + mirrorReference.getTarget() + "/issues";
    }

    @Override
    public String createLink(MirrorReference mirrorReference) {
        return "https://github.com/" + mirrorReference.getTarget() + "/issues";
    }
}

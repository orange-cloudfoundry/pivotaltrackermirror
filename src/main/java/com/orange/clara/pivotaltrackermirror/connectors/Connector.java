package com.orange.clara.pivotaltrackermirror.connectors;

import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorException;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import onespot.pivotal.api.resources.Comment;
import onespot.pivotal.api.resources.Story;

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
public interface Connector<T, P> {

    T postOrUpdateStory(MirrorReference mirrorReference, T story) throws ConnectorException;

    P postOrUpdateComment(MirrorReference mirrorReference, T story, P comment) throws ConnectorException;

    T convertStory(Story story);

    P convertComment(Comment comment);
}

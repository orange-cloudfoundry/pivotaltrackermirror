package com.orange.clara.pivotaltrackermirror.converter;

import com.orange.clara.pivotaltrackermirror.connectors.Connector;
import com.orange.clara.pivotaltrackermirror.exceptions.ConnectorException;
import com.orange.clara.pivotaltrackermirror.exceptions.ConvertException;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.model.StoryCompleteReference;
import onespot.pivotal.api.resources.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class AbstractPivotalTrackerConverter<T, P> implements PivotalTrackerConverter {
    protected Connector<T, P> connector;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AbstractPivotalTrackerConverter(Connector<T, P> connector) {
        this.connector = connector;
    }

    @Override
    public void convert(MirrorReference mirrorReference, StoryCompleteReference storyCompleteReference) throws ConvertException {
        logger.debug("Converting {}", storyCompleteReference.getId());
        T convertedStory = this.connector.convertStory(storyCompleteReference);
        try {
            T finalStory = this.sendStory(mirrorReference, convertedStory);
            this.sendComments(mirrorReference, finalStory, storyCompleteReference.getComments());
        } catch (Exception e) {
            throw new ConvertException(e.getMessage(), e);
        }
        logger.debug("Finished Converting {}", storyCompleteReference.getId());
    }

    @Override
    public void convert(MirrorReference mirrorReference, List<StoryCompleteReference> storyCompleteReferences) throws ConvertException {
        for (StoryCompleteReference storyCompleteReference : storyCompleteReferences) {
            this.convert(mirrorReference, storyCompleteReference);
        }
    }

    protected T sendStory(MirrorReference mirrorReference, T convertedStory) throws ConnectorException {
        return this.connector.postOrUpdateStory(mirrorReference, convertedStory);
    }

    protected void sendComments(MirrorReference mirrorReference, T story, List<Comment> comments) throws ConnectorException {
        for (Comment comment : comments) {
            this.connector.postOrUpdateComment(mirrorReference, story, this.connector.convertComment(comment));
        }
    }


}

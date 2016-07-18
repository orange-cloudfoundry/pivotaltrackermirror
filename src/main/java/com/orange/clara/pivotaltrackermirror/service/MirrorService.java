package com.orange.clara.pivotaltrackermirror.service;

import com.google.common.collect.Lists;
import com.orange.clara.pivotaltrackermirror.converter.PivotalTrackerConverter;
import com.orange.clara.pivotaltrackermirror.converter.factory.PivotalTrackerConverterFactory;
import com.orange.clara.pivotaltrackermirror.exceptions.CannotFindConverterException;
import com.orange.clara.pivotaltrackermirror.exceptions.ConvertException;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.model.StoryCompleteReference;
import onespot.pivotal.api.PivotalTracker;
import onespot.pivotal.api.dao.ProjectDAO;
import onespot.pivotal.api.resources.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class MirrorService {

    @Autowired
    private PivotalTracker pivotalTracker;

    @Autowired
    private PivotalTrackerConverterFactory converterFactory;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void mirror(MirrorReference mirrorReference) throws CannotFindConverterException, ConvertException {
        PivotalTrackerConverter converter = converterFactory.getPivotalTrackerConverter(mirrorReference);
        ProjectDAO projectDAO = pivotalTracker.projects().id(mirrorReference.getPivotalTrackerProjectId());
        logger.debug("Getting stories for project {}", mirrorReference.getPivotalTrackerProjectId());
        List<Story> stories;
        if (mirrorReference.getUpdatedAt() == null) {
            stories = Lists.newArrayList();
            stories.addAll(projectDAO.stories().withState(Story.StoryState.delivered).get());
            stories.addAll(projectDAO.stories().withState(Story.StoryState.finished).get());
            stories.addAll(projectDAO.stories().withState(Story.StoryState.planned).get());
            stories.addAll(projectDAO.stories().withState(Story.StoryState.rejected).get());
            stories.addAll(projectDAO.stories().withState(Story.StoryState.started).get());
            stories.addAll(projectDAO.stories().withState(Story.StoryState.unscheduled).get());
            stories.addAll(projectDAO.stories().withState(Story.StoryState.unstarted).get());
        } else {
            stories = projectDAO.stories().updatedAfter(mirrorReference.getUpdatedAt().toInstant()).get();
        }
        logger.debug("Finished getting stories for project {}.", mirrorReference.getPivotalTrackerProjectId());
        List<StoryCompleteReference> storyCompleteReferences = Lists.newArrayList();
        for (Story story : stories) {
            storyCompleteReferences.add(
                    new StoryCompleteReference(
                            story,
                            projectDAO.stories().id(story.getId()).comments().get()
                    ));
        }
        converter.convert(mirrorReference, storyCompleteReferences);
    }
}

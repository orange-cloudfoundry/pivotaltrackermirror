package com.orange.clara.pivotaltrackermirror.converter;

import com.orange.clara.pivotaltrackermirror.exceptions.ConvertException;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.model.StoryCompleteReference;

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
public interface PivotalTrackerConverter {
    void convert(MirrorReference mirrorReference, StoryCompleteReference storyCompleteReference, String token) throws ConvertException;

    void convert(MirrorReference mirrorReference, List<StoryCompleteReference> storyCompleteReferences, String token) throws ConvertException;

    String createLink(MirrorReference mirrorReference);
}

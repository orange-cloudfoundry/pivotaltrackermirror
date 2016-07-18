package com.orange.clara.pivotaltrackermirror.converter.factory;

import com.orange.clara.pivotaltrackermirror.converter.PivotalTrackerConverter;
import com.orange.clara.pivotaltrackermirror.converter.PivotalTrackerToGithubConverter;
import com.orange.clara.pivotaltrackermirror.exceptions.CannotFindConverterException;
import com.orange.clara.pivotaltrackermirror.model.ConverterType;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

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
@Component
public class PivotalTrackerConverterFactory {

    @Autowired
    private PivotalTrackerToGithubConverter pivotalTrackerToGithubConverter;

    private Map<ConverterType, PivotalTrackerConverter> converters;

    @PostConstruct
    public void convertersLoad() {
        converters = new HashMap<>();
        converters.put(ConverterType.GITHUB, pivotalTrackerToGithubConverter);
    }

    public PivotalTrackerConverter getPivotalTrackerConverter(ConverterType converterType) {
        return this.converters.get(converterType);
    }

    public PivotalTrackerConverter getPivotalTrackerConverter(MirrorReference mirrorReference) throws CannotFindConverterException {
        PivotalTrackerConverter converter = this.converters.get(mirrorReference.getType());
        if (converter == null) {
            throw new CannotFindConverterException("Cannot find converter " + mirrorReference.getType());
        }
        return converter;
    }
}

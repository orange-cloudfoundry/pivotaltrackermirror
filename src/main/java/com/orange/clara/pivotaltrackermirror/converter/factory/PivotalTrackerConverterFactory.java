package com.orange.clara.pivotaltrackermirror.converter.factory;

import com.orange.clara.pivotaltrackermirror.converter.PivotalTrackerConverter;
import com.orange.clara.pivotaltrackermirror.converter.PivotalTrackerToGithubConverter;
import com.orange.clara.pivotaltrackermirror.exceptions.CannotFindConverterException;
import com.orange.clara.pivotaltrackermirror.model.ConverterType;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
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

    private Map<ConverterType, Class<? extends PivotalTrackerConverter>> converters;

    @PostConstruct
    public void convertersLoad() {
        converters = new HashMap<>();
        converters.put(ConverterType.GITHUB, PivotalTrackerToGithubConverter.class);
    }

    public PivotalTrackerConverter getPivotalTrackerConverter(ConverterType converterType) throws IllegalAccessException, InstantiationException {
        return this.converters.get(converterType).newInstance();
    }

    public PivotalTrackerConverter getPivotalTrackerConverter(MirrorReference mirrorReference) throws CannotFindConverterException, InstantiationException, IllegalAccessException {
        PivotalTrackerConverter converter = getPivotalTrackerConverter(mirrorReference.getType());
        if (converter == null) {
            throw new CannotFindConverterException("Cannot find converter " + mirrorReference.getType());
        }
        return converter;
    }
}

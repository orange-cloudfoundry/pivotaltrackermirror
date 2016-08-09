package com.orange.clara.pivotaltrackermirror.controllers;

import com.google.common.collect.Lists;
import com.orange.clara.pivotaltrackermirror.converter.PivotalTrackerConverter;
import com.orange.clara.pivotaltrackermirror.converter.factory.PivotalTrackerConverterFactory;
import com.orange.clara.pivotaltrackermirror.exceptions.CannotFindConverterException;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.model.response.MirrorReferenceResponse;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.TimeZone;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 18/07/2016
 */
@Controller
public class UiController extends AbstractController {

    @Autowired
    private PivotalTrackerConverterFactory converterFactory;

    @RequestMapping("/")
    public String index(Model model, TimeZone timeZone) throws SchedulerException, CannotFindConverterException, IllegalAccessException, InstantiationException {
        List<MirrorReferenceResponse> mirrorReferenceResponseList = Lists.newArrayList();
        Iterable<MirrorReference> mirrorReferences = this.mirrorReferenceRepo.findAll();
        for (MirrorReference mirrorReference : mirrorReferences) {
            PivotalTrackerConverter pivotalTrackerConverter = converterFactory.getPivotalTrackerConverter(mirrorReference);
            MirrorReferenceResponse mirrorReferenceResponse = new MirrorReferenceResponse(mirrorReference, this.getTriggerState(mirrorReference.getId()));
            mirrorReferenceResponse.setTargetLink(pivotalTrackerConverter.createLink(mirrorReference));
            mirrorReferenceResponseList.add(mirrorReferenceResponse);
        }
        model.addAttribute("timeZone", timeZone);
        model.addAttribute("mirrorReferences", mirrorReferenceResponseList);
        return "index";
    }
}

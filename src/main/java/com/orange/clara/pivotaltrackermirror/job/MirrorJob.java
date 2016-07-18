package com.orange.clara.pivotaltrackermirror.job;

import com.orange.clara.pivotaltrackermirror.exceptions.CannotFindConverterException;
import com.orange.clara.pivotaltrackermirror.exceptions.ConvertException;
import com.orange.clara.pivotaltrackermirror.exceptions.MirrorJobExecutionException;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.repos.MirrorReferenceRepo;
import com.orange.clara.pivotaltrackermirror.service.MirrorService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Calendar;

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
public class MirrorJob implements Job {

    public final static String JOB_KEY_NAME = "jobFor";
    public final static String JOB_KEY_GROUP = "mirrorJob";
    public final static String TRIGGER_KEY_NAME = "mirror";
    public final static String TRIGGER_KEY_GROUP = "mirrorTrigger";
    public final static String JOB_MIRROR_REFERENCE_ID_KEY = "mirrorReferenceId";

    @Autowired
    private MirrorService mirrorService;
    @Autowired
    private MirrorReferenceRepo mirrorReferenceRepo;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String mirrorReferenceId = (String) dataMap.get(JOB_MIRROR_REFERENCE_ID_KEY);
        MirrorReference mirrorReference = mirrorReferenceRepo.findOne(Integer.valueOf(mirrorReferenceId));
        if (mirrorReference == null) {
            return;
        }
        try {
            this.mirrorService.mirror(mirrorReference);
        } catch (CannotFindConverterException | ConvertException e) {
            throw new MirrorJobExecutionException(e.getMessage(), e, false);
        }
        mirrorReference.setUpdatedAt(Calendar.getInstance().getTime());
        this.mirrorReferenceRepo.save(mirrorReference);
    }


    public void setMirrorService(MirrorService mirrorService) {
        this.mirrorService = mirrorService;
    }


    public void setMirrorReferenceRepo(MirrorReferenceRepo mirrorReferenceRepo) {
        this.mirrorReferenceRepo = mirrorReferenceRepo;
    }
}
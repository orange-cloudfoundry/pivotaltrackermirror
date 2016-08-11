package com.orange.clara.pivotaltrackermirror.controllers;

import com.google.common.collect.Lists;
import com.orange.clara.pivotaltrackermirror.exceptions.CannotFindConverterException;
import com.orange.clara.pivotaltrackermirror.exceptions.ConvertException;
import com.orange.clara.pivotaltrackermirror.job.MirrorJob;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.model.request.MirrorReferenceRequest;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;


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
@RestController
@RequestMapping("/api/mirrorReferences")
public class MirrorReferenceController extends AbstractController {

    @Autowired
    @Qualifier("appUrl")
    private String appUrl;
    @Autowired
    @Qualifier("refreshMirrorMinutes")
    private Integer refreshMirrorMinutes;

    @RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody MirrorReferenceRequest mirrorReferenceRequest) throws ConvertException, CannotFindConverterException, SchedulerException {
        MirrorReference mirrorReference = mirrorReferenceRequest.toMirrorReference();
        mirrorReference.setUpdatedAt(null);
        mirrorReference = this.mirrorReferenceRepo.save(mirrorReference);
        JobDetail job = JobBuilder.newJob(MirrorJob.class)
                .withIdentity(MirrorJob.JOB_KEY_NAME + mirrorReference.getId(), MirrorJob.JOB_KEY_GROUP)
                .usingJobData(MirrorJob.JOB_MIRROR_REFERENCE_ID_KEY, String.valueOf(mirrorReference.getId()))
                .usingJobData(MirrorJob.JOB_MIRROR_TOKEN_KEY, mirrorReferenceRequest.getToken())
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(MirrorJob.TRIGGER_KEY_NAME + mirrorReference.getId(), MirrorJob.TRIGGER_KEY_GROUP)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(refreshMirrorMinutes)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(job, trigger);
        return ResponseEntity.created(URI.create(appUrl + "/api/task/" + mirrorReference.getId() + "/status")).body(mirrorReference);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]*}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable Integer id) {
        MirrorReference mirrorReference = this.mirrorReferenceRepo.findOne(id);
        if (mirrorReference == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mirrorReference);
    }

    @RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(Lists.newArrayList(this.mirrorReferenceRepo.findAll()));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:[0-9]*}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Integer id) throws SchedulerException {
        MirrorReference mirrorReference = this.mirrorReferenceRepo.findOne(id);
        if (mirrorReference == null) {
            return ResponseEntity.notFound().build();
        }
        scheduler.deleteJob(new JobKey(MirrorJob.JOB_KEY_NAME + mirrorReference.getId(), MirrorJob.JOB_KEY_GROUP));
        this.mirrorReferenceRepo.delete(mirrorReference);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]*}/force-update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> forceUpdate(@PathVariable Integer id) throws SchedulerException {
        MirrorReference mirrorReference = this.mirrorReferenceRepo.findOne(id);
        if (mirrorReference == null) {
            return ResponseEntity.notFound().build();
        }
        mirrorReference.setUpdatedAt(new Date(0L));
        this.mirrorReferenceRepo.save(mirrorReference);
        JobKey jobKey = new JobKey(MirrorJob.TRIGGER_KEY_NAME + id, MirrorJob.TRIGGER_KEY_GROUP);
        scheduler.triggerJob(jobKey);
        return ResponseEntity.accepted().build();
    }
}

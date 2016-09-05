package com.orange.clara.pivotaltrackermirror.controllers;

import com.google.common.collect.Lists;
import com.orange.clara.pivotaltrackermirror.job.MirrorJob;
import com.orange.clara.pivotaltrackermirror.model.JobStatus;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.model.response.TriggerResponse;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
@RestController
@RequestMapping("/api/tasks")
public class TaskStatusController extends AbstractController {

    @ApiOperation(value = "Get the current status of the job running for a specific mirror", response = JobStatus.class)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStatus(@PathVariable Integer id) throws SchedulerException {
        return ResponseEntity.ok(this.getTriggerState(id));
    }

    @ApiOperation(value = "Get information of a task which is link to a specific mirror", response = TriggerResponse.class)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable Integer id) throws SchedulerException {
        Trigger trigger = this.getTriggerFromId(id);
        return ResponseEntity.ok(new TriggerResponse(trigger, this.getTriggerState(id), id));
    }

    @ApiOperation(value = "Get information of all tasks.", response = TriggerResponse.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET, value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() throws SchedulerException {
        Iterable<MirrorReference> mirrorReferences = this.mirrorReferenceRepo.findAll();
        List<TriggerResponse> triggerResponseList = Lists.newArrayList();
        for (MirrorReference mirrorReference : mirrorReferences) {
            Trigger trigger = this.getTriggerFromId(mirrorReference.getId());
            triggerResponseList.add(new TriggerResponse(trigger, this.getTriggerState(mirrorReference.getId()), mirrorReference.getId()));
        }
        return ResponseEntity.ok(triggerResponseList);
    }

    private Trigger getTriggerFromId(Integer id) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(MirrorJob.TRIGGER_KEY_NAME + id, MirrorJob.TRIGGER_KEY_GROUP);
        return scheduler.getTrigger(triggerKey);
    }
}

package com.orange.clara.pivotaltrackermirror.controllers;

import com.orange.clara.pivotaltrackermirror.job.MirrorJob;
import com.orange.clara.pivotaltrackermirror.model.response.TriggerResponse;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStatus(@PathVariable Integer id) throws SchedulerException {
        return ResponseEntity.ok(this.getTriggerState(id));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable Integer id) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(MirrorJob.TRIGGER_KEY_NAME + id, MirrorJob.TRIGGER_KEY_GROUP);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        return ResponseEntity.ok(new TriggerResponse(trigger, this.getTriggerState(id)));
    }
}

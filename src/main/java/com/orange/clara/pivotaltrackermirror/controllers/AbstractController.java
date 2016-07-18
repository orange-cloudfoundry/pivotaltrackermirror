package com.orange.clara.pivotaltrackermirror.controllers;

import com.orange.clara.pivotaltrackermirror.job.MirrorJob;
import com.orange.clara.pivotaltrackermirror.model.JobStatus;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import com.orange.clara.pivotaltrackermirror.repos.MirrorReferenceRepo;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

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
public abstract class AbstractController {
    @Autowired
    protected Scheduler scheduler;
    @Autowired
    protected MirrorReferenceRepo mirrorReferenceRepo;

    protected JobStatus getTriggerState(Integer id) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(MirrorJob.TRIGGER_KEY_NAME + id, MirrorJob.TRIGGER_KEY_GROUP);
        MirrorReference mirrorReference = this.mirrorReferenceRepo.findOne(id);
        if (mirrorReference == null) {
            return JobStatus.valueOf(scheduler.getTriggerState(triggerKey).name());
        }
        if (mirrorReference.getUpdatedAt() == null) {
            return JobStatus.RUNNING;
        }
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        if (triggerState.equals(Trigger.TriggerState.BLOCKED)
                || triggerState.equals(Trigger.TriggerState.ERROR)
                || triggerState.equals(Trigger.TriggerState.PAUSED)
                || triggerState.equals(Trigger.TriggerState.COMPLETE)) {
            return JobStatus.valueOf(scheduler.getTriggerState(triggerKey).name());
        }
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger.getPreviousFireTime().before(mirrorReference.getUpdatedAt())
                || trigger.getPreviousFireTime().equals(mirrorReference.getUpdatedAt())) {
            return JobStatus.COMPLETE;
        }
        return JobStatus.RUNNING;
    }
}

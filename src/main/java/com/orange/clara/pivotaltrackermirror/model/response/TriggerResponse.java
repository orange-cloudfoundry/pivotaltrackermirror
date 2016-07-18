package com.orange.clara.pivotaltrackermirror.model.response;

import com.orange.clara.pivotaltrackermirror.model.JobStatus;
import org.quartz.Trigger;

import java.util.Date;

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
public class TriggerResponse {

    private JobStatus jobStatus;

    private Date dateStartTime;

    private Date nextFireTime;

    private Date previousFireTime;

    public TriggerResponse(Trigger trigger, JobStatus jobStatus) {
        this.dateStartTime = trigger.getStartTime();
        this.nextFireTime = trigger.getNextFireTime();
        this.previousFireTime = trigger.getPreviousFireTime();
        this.jobStatus = jobStatus;
    }


    public Date getDateStartTime() {
        return dateStartTime;
    }

    public void setDateStartTime(Date dateStartTime) {
        this.dateStartTime = dateStartTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }
}

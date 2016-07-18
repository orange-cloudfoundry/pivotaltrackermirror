package com.orange.clara.pivotaltrackermirror.model.response;

import com.orange.clara.pivotaltrackermirror.model.JobStatus;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;

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
public class MirrorReferenceResponse extends MirrorReference {
    private JobStatus jobStatus;

    private String targetLink;

    public MirrorReferenceResponse(MirrorReference mirrorReference, JobStatus jobStatus) {
        this.id = mirrorReference.getId();
        this.pivotalTrackerProjectId = mirrorReference.getPivotalTrackerProjectId();
        this.secret = mirrorReference.getSecret();
        this.target = mirrorReference.getTarget();
        this.type = mirrorReference.getType();
        this.updatedAt = mirrorReference.getUpdatedAt();
        this.jobStatus = jobStatus;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getTargetLink() {
        return targetLink;
    }

    public void setTargetLink(String targetLink) {
        this.targetLink = targetLink;
    }
}

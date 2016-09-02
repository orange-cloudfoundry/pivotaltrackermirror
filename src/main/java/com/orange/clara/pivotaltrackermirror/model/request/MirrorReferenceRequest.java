package com.orange.clara.pivotaltrackermirror.model.request;

import com.orange.clara.pivotaltrackermirror.model.ConverterType;
import com.orange.clara.pivotaltrackermirror.model.JobStatus;
import com.orange.clara.pivotaltrackermirror.model.MirrorReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 27/07/2016
 */
@ApiModel(value = "Mirror Reference description")
public class MirrorReferenceRequest extends MirrorReference {


    private String token;

    public String getToken() {
        return token;
    }

    @ApiModelProperty(value = "Converter token (e.g.: github token)", required = true)
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    @ApiModelProperty(value = "Pivotal tracker project id", required = true)
    public void setPivotalTrackerProjectId(int pivotalTrackerProjectId) {
        super.setPivotalTrackerProjectId(pivotalTrackerProjectId);
    }

    @Override
    @ApiModelProperty(value = "Converter target (e.g.: a github repo)", required = true)
    public void setTarget(String target) {
        super.setTarget(target);
    }

    @Override
    @ApiModelProperty(hidden = true)
    public void setUpdatedAt(Date updatedAt) {
        super.setUpdatedAt(updatedAt);
    }

    @Override
    @ApiModelProperty(hidden = true)
    public void setSecret(String secret) {
        super.setSecret(secret);
    }

    @Override
    @ApiModelProperty(value = "Converter to use (e.g.: github)", required = true)
    public void setType(ConverterType type) {
        super.setType(type);
    }

    @Override
    @ApiModelProperty(hidden = true)
    public void setLastJobStatus(JobStatus lastJobStatus) {
        super.setLastJobStatus(lastJobStatus);
    }

    @Override
    @ApiModelProperty(hidden = true)
    public void setLastJobErrorMessage(String lastJobErrorMessage) {
        super.setLastJobErrorMessage(lastJobErrorMessage);
    }

    public MirrorReference toMirrorReference() {
        MirrorReference mirrorReference = new MirrorReference();
        mirrorReference.setType(this.type);
        mirrorReference.setId(this.id);
        mirrorReference.setTarget(this.target);
        mirrorReference.setUpdatedAt(this.updatedAt);
        mirrorReference.setPivotalTrackerProjectId(this.pivotalTrackerProjectId);
        mirrorReference.setSecret(this.secret);
        return mirrorReference;
    }
}

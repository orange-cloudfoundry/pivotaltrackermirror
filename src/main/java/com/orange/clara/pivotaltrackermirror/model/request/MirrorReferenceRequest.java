package com.orange.clara.pivotaltrackermirror.model.request;

import com.orange.clara.pivotaltrackermirror.model.MirrorReference;

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
public class MirrorReferenceRequest extends MirrorReference {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

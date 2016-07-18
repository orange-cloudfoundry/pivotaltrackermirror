package com.orange.clara.pivotaltrackermirror.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 13/07/2016
 */
@Entity
public class MirrorReference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;

    protected int pivotalTrackerProjectId;

    protected String target;

    protected Date updatedAt;


    @Enumerated(EnumType.STRING)
    protected ConverterType type;

    protected String secret;

    public MirrorReference() {
        updatedAt = Calendar.getInstance().getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPivotalTrackerProjectId() {
        return pivotalTrackerProjectId;
    }

    public void setPivotalTrackerProjectId(int pivotalTrackerProjectId) {
        this.pivotalTrackerProjectId = pivotalTrackerProjectId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public ConverterType getType() {
        return type;
    }

    public void setType(ConverterType type) {
        this.type = type;
    }
}

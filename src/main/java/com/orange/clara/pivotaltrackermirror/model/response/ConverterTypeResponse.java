package com.orange.clara.pivotaltrackermirror.model.response;

import com.orange.clara.pivotaltrackermirror.model.ConverterType;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 29/07/2016
 */
public class ConverterTypeResponse {
    private String name;
    private boolean isUseToken;

    public ConverterTypeResponse(ConverterType converterType) {
        this.name = converterType.name();
        this.isUseToken = converterType.isUseToken();
    }

    public ConverterTypeResponse(String name, boolean isUseToken) {
        this.name = name;
        this.isUseToken = isUseToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsUseToken() {
        return isUseToken;
    }

    public void setIsUseToken(boolean isUseToken) {
        this.isUseToken = isUseToken;
    }
}

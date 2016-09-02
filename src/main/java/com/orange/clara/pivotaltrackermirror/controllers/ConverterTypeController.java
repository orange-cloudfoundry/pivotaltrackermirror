package com.orange.clara.pivotaltrackermirror.controllers;

import com.google.common.collect.Lists;
import com.orange.clara.pivotaltrackermirror.model.ConverterType;
import com.orange.clara.pivotaltrackermirror.model.response.ConverterTypeResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * Date: 29/07/2016
 */
@RestController
@RequestMapping("/api/converterTypes")
public class ConverterTypeController {

    @ApiOperation("Get the list of all available converter in the app")
    @RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        List<ConverterTypeResponse> converterTypeResponses = Lists.newArrayList();
        for (ConverterType converterType : ConverterType.values()) {
            converterTypeResponses.add(new ConverterTypeResponse(converterType));
        }
        return ResponseEntity.ok(converterTypeResponses);
    }
}

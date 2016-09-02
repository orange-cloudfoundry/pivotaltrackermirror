package com.orange.clara.pivotaltrackermirror.util;

import com.orange.clara.pivotaltrackermirror.PivotaltrackerMirror;
import io.github.swagger2markup.Swagger2MarkupConverter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 02/09/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PivotaltrackerMirror.class)
@WebIntegrationTest
public class Swagger2MarkupTest {

    @Test
    @Ignore
    public void convertRemoteSwaggerToAsciiDoc() throws MalformedURLException {
        // Remote Swagger source
        Swagger2MarkupConverter.from(new URL("http://localhost:8080/swagger/api-docs")).build()
                .toFile(Paths.get("src/docs/asciidoc/generated/api"));
    }
}
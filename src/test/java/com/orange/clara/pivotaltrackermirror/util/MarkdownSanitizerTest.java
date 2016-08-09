package com.orange.clara.pivotaltrackermirror.util;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Copyright (C) 2016 Arthur Halet
 * <p>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p>
 * Author: Arthur Halet
 * Date: 27/07/2016
 */
public class MarkdownSanitizerTest {
    @Test
    public void sanitizeMail() throws Exception {
        String text = "hey my name is \n john <john@gmail.com> \n or toto toto@gmail.com \n but not titi @titi";
        String expectedText = "hey my name is \n john <*hidden mail*> \n or toto *hidden mail* \n but not titi @titi";
        assertThat(MarkdownSanitizer.sanitizeMail(text)).isEqualTo(expectedText);
    }

    @Test
    public void sanitizeBunchOfText() {
        String text = "Github Issue 240:\n" +
                "```\n" +
                "    Hello,\n" +
                "\n" +
                "    I tried the ltc test -v and I have the all three tests fail. Here is the full output:\n" +
                "\n" +
                "    ➜  vagrant  ./ltc test -v\n" +
                "    Running Suite: Lattice Integration Tests\n" +
                "    ========================================\n" +
                "    Random Seed: 1453896523\n" +
                "    Will run 3 of 3 specs\n" +
                "\n" +
                "    Lattice cluster docker apps with HTTP routes\n" +
                "     should run with the provided ltc options\n" +
                "     /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:147\n" +
                "    [test] Attempting to stream cluster debug logs\n" +
                "    [test] Attempting to create lattice-test-app-1373fd78-f8db-486a-6d68-a6f6ca28a156\n" +
                "    [create] Error fetching image metadata: Error while pulling image: Get https://index.docker.io/v1/repositories/cloudfoundry/lattice-app/images: dial tcp 54.164.250.255:443: getsockopt: connection refused\n" +
                "    [test] Attempting to remove app lattice-test-app-1373fd78-f8db-486a-6d68-a6f6ca28a156\n" +
                "    [remove] Removing lattice-test-app-1373fd78-f8db-486a-6d68-a6f6ca28a156...\n" +
                "    [remove] Error stopping lattice-test-app-1373fd78-f8db-486a-6d68-a6f6ca28a156: lattice-test-app-1373fd78-f8db-486a-6d68-a6f6ca28a156 is not started.\n" +
                "\n" +
                "    • Failure [0.218 seconds]\n" +
                "    Lattice cluster\n" +
                "    /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:231\n" +
                "     docker apps with HTTP routes\n" +
                "     /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:148\n" +
                "       should run with the provided ltc options [It]\n" +
                "       /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:147\n" +
                "\n" +
                "       No future change is possible.  Bailing out early after 0.157s.\n" +
                "       Expected\n" +
                "           <int>: 15\n" +
                "       to match exit code:\n" +
                "           <int>: 0\n" +
                "\n" +
                "       /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:499\n" +
                "    ------------------------------\n" +
                "    Lattice cluster docker apps with TCP routes\n" +
                "     should run with the provided ltc options\n" +
                "     /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:173\n" +
                "    [test] Attempting to create lattice-test-app-180adcee-2974-420e-679c-056963444b5f\n" +
                "    [create] Error fetching image metadata: Error while pulling image: Get https://index.docker.io/v1/repositories/cloudfoundry/lattice-tcp-test/images: dial tcp 52.0.10.162:443: getsockopt: connection refused\n" +
                "    [test] Attempting to remove app lattice-test-app-180adcee-2974-420e-679c-056963444b5f\n" +
                "    [remove] Removing lattice-test-app-180adcee-2974-420e-679c-056963444b5f...\n" +
                "    [remove] Error stopping lattice-test-app-180adcee-2974-420e-679c-056963444b5f: lattice-test-app-180adcee-2974-420e-679c-056963444b5f is not started.\n" +
                "\n" +
                "    • Failure [0.099 seconds]\n" +
                "    Lattice cluster\n" +
                "    /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:231\n" +
                "     docker apps with TCP routes\n" +
                "     /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:174\n" +
                "       should run with the provided ltc options [It]\n" +
                "       /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:173\n" +
                "\n" +
                "       No future change is possible.  Bailing out early after 0.032s.\n" +
                "       Expected\n" +
                "           <int>: 15\n" +
                "       to match exit code:\n" +
                "           <int>: 0\n" +
                "\n" +
                "       /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:499\n" +
                "    ------------------------------\n" +
                "    Lattice cluster droplet apps\n" +
                "     builds, lists and launches a droplet\n" +
                "     /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:229\n" +
                "    STEP: checking out lattice-app from github\n" +
                "    [test] Attempting to clone https://github.com/cloudfoundry-samples/lattice-app.git to /var/folders/dd/yvwrqnxj4zl80hg264skbcbxnqvgl2/T/repo427798934\n" +
                "    [git-clone] Cloning into '/var/folders/dd/yvwrqnxj4zl80hg264skbcbxnqvgl2/T/repo427798934'...\n" +
                "    [git-clone] fatal: unable to access 'https://github.com/cloudfoundry-samples/lattice-app.git/': Failed to connect to github.com port 443: Connection refused\n" +
                "    [test] Attempting to remove app running-droplet-5384f8bf-006d-4106-56ed-edfbbdd76f0d\n" +
                "    [remove] Removing running-droplet-5384f8bf-006d-4106-56ed-edfbbdd76f0d...\n" +
                "    [remove] Error stopping running-droplet-5384f8bf-006d-4106-56ed-edfbbdd76f0d: running-droplet-5384f8bf-006d-4106-56ed-edfbbdd76f0d is not started.\n" +
                "\n" +
                "    • Failure [0.174 seconds]\n" +
                "    Lattice cluster\n" +
                "    /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:231\n" +
                "     droplet apps\n" +
                "     /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:230\n" +
                "       builds, lists and launches a droplet [It]\n" +
                "       /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:229\n" +
                "\n" +
                "       No future change is possible.  Bailing out early after 0.117s.\n" +
                "       Expected\n" +
                "           <int>: 128\n" +
                "       to match exit code:\n" +
                "           <int>: 0\n" +
                "\n" +
                "       /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:499\n" +
                "    ------------------------------\n" +
                "\n" +
                "\n" +
                "    Summarizing 3 Failures:\n" +
                "\n" +
                "    [Fail] Lattice cluster docker apps with HTTP routes [It] should run with the provided ltc options\n" +
                "    /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:499\n" +
                "\n" +
                "    [Fail] Lattice cluster docker apps with TCP routes [It] should run with the provided ltc options\n" +
                "    /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:499\n" +
                "\n" +
                "    [Fail] Lattice cluster droplet apps [It] builds, lists and launches a droplet\n" +
                "    /tmp/build/8ae14b12-4968-4e09-78b1-4e6bd3b9bc46/lattice-release/src/github.com/cloudfoundry-incubator/ltc/cluster_test/cluster_test_runner.go:499\n" +
                "\n" +
                "    Ran 3 of 3 Specs in 0.492 seconds\n" +
                "    FAIL! -- 0 Passed | 3 Failed | 0 Pending | 0 Skipped\n" +
                "```\n" +
                "Filed by [ihr](https://github.com/ihr)";
        System.out.println(MarkdownSanitizer.sanitize(text));
    }

    @Test
    public void sanitizeMention() throws Exception {
        String text = "hey my name is \n @john my github account is [@john](https://github.com/john)\n@titi";
        String expectedText = "hey my name is \n " + MarkdownSanitizer.PT_USER_ICON + "john my github account is "
                + MarkdownSanitizer.GITHUB_USER_ICON + "[@john](https://github.com/john)\n" + MarkdownSanitizer.PT_USER_ICON + "titi";
        assertThat(MarkdownSanitizer.sanitizeMention(text)).isEqualTo(expectedText);
    }
}
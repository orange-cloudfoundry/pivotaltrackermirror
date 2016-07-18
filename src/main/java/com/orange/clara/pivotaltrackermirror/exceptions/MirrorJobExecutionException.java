package com.orange.clara.pivotaltrackermirror.exceptions;

import org.quartz.JobExecutionException;

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
public class MirrorJobExecutionException extends JobExecutionException {
    public MirrorJobExecutionException() {
    }

    public MirrorJobExecutionException(Throwable cause) {
        super(cause);
    }

    public MirrorJobExecutionException(String msg) {
        super(msg);
    }

    public MirrorJobExecutionException(boolean refireImmediately) {
        super(refireImmediately);
    }

    public MirrorJobExecutionException(Throwable cause, boolean refireImmediately) {
        super(cause, refireImmediately);
    }

    public MirrorJobExecutionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MirrorJobExecutionException(String msg, Throwable cause, boolean refireImmediately) {
        super(msg, cause, refireImmediately);
    }

    public MirrorJobExecutionException(String msg, boolean refireImmediately) {
        super(msg, refireImmediately);
    }
}

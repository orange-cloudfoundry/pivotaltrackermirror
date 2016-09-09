package com.orange.clara.pivotaltrackermirror.util;

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
public class MarkdownSanitizer {
    public static final String EMAIL_PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z0-9]{2,6}";
    public static final String MENTION_GITHUB_USER = "\\[(\\w|@)+\\]\\(https://github\\.com/([^/]+)\\)";
    public static final String MENTION_PT_USER = "(?m)(^| +)@(\\w+)";

    public static String sanitize(String text) {
        if (text == null) {
            return "";
        }
        text = sanitizeMention(sanitizeMail(text));
        return text;
    }

    public static String sanitizeMail(String text) {
        return text.replaceAll(EMAIL_PATTERN, "*hidden mail*");
    }

    public static String sanitizeMention(String text) {
        return sanitizePtMention(sanitizeGithubMention(text));
    }

    public static String sanitizeGithubMention(String text) {
        return text.replaceAll(MENTION_GITHUB_USER, "`@gh:$2`");
    }

    public static String sanitizePtMention(String text) {
        return text.replaceAll(MENTION_PT_USER, "$1`@pt:$2`");
    }
}

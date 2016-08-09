package com.orange.clara.pivotaltrackermirror.util;

import java.util.regex.Matcher;

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
    public static final String MENTION_GITHUB_USER = "\\[(\\w|@)+\\]\\(https://github.com/[^/]+\\)";
    public static final String MENTION_PT_USER = "(?m)(^| +)@(\\w+)";
    public static final String GITHUB_USER_ICON = "![Image of github user](https://camo.githubusercontent.com/d640cef2811e3de84ab7d41db592bb0d3c4c73c3/687474703a2f2f6c6f7569736875616e672e696e666f2f696d616765732f736f6369616c5f69636f6e2f6769746875622e706e67)";
    public static final String PT_USER_ICON = "![Image of pivotal tracker](https://camo.githubusercontent.com/8b4d4f5980bb56135a9c5d1a555ddfcccdb2952b/68747470733a2f2f6c68332e676f6f676c6575736572636f6e74656e742e636f6d2f2d506f466c494633716c66302f41414141414141414141492f414141414141414141474d2f643239495f7a4b637953512f7334362d632d6b2d6e6f2f70686f746f2e6a7067)";

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
        return text.replaceAll(MENTION_GITHUB_USER, Matcher.quoteReplacement(GITHUB_USER_ICON) + "$0");
    }

    public static String sanitizePtMention(String text) {
        return text.replaceAll(MENTION_PT_USER, "$1" + Matcher.quoteReplacement(PT_USER_ICON) + "$2");
    }
}

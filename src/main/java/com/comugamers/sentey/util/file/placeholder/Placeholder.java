package com.comugamers.sentey.util.file.placeholder;

public class Placeholder {

    private final String value;
    private final String replacement;

    public Placeholder(String value, Object object) {
        this.value = value;
        this.replacement = String.valueOf(object);
    }

    public String getValue() {
        return value;
    }

    public String getReplacement() {
        return replacement;
    }
}
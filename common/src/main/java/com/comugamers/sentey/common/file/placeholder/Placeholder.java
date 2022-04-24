package com.comugamers.sentey.common.file.placeholder;

public class Placeholder {

    private final String value;
    private final String replacement;

    public Placeholder(String value, String replacement) {
        this.value = value;
        this.replacement = replacement;
    }

    public Placeholder(String value, int replacement) {
        this.value = value;
        this.replacement = String.valueOf(replacement);
    }

    public Placeholder(String value, double replacement) {
        this.value = value;
        this.replacement = String.valueOf(replacement);
    }

    public Placeholder(String value, float replacement) {
        this.value = value;
        this.replacement = String.valueOf(replacement);
    }

    public Placeholder(String value, long replacement) {
        this.value = value;
        this.replacement = String.valueOf(replacement);
    }

    public Placeholder(String value, short replacement) {
        this.value = value;
        this.replacement = String.valueOf(replacement);
    }

    public Placeholder(String value, byte replacement) {
        this.value = value;
        this.replacement = String.valueOf(replacement);
    }

    public Placeholder(String value, char replacement) {
        this.value = value;
        this.replacement = String.valueOf(replacement);
    }

    public Placeholder(String value, boolean replacement) {
        this.value = value;
        this.replacement = String.valueOf(replacement);
    }

    public Placeholder(String value, Object object) {
        this.value = value;
        this.replacement = object.toString();
    }

    public String getValue() {
        return value;
    }

    public String getReplacement() {
        return replacement;
    }
}
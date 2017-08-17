package com.plainrequest.enums;

/**
 * @author Giovani Moura
 */
public enum ContentTypeEnum {

    APPLICATION_JSON("application/json"),
    APPLICATION_FROM_URLENCODED("application/x-www-form-urlencoded");

    private String value;

    ContentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
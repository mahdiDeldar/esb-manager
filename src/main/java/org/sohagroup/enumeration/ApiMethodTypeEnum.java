package org.sohagroup.enumeration;

public enum ApiMethodTypeEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT");
    private final String value;

    ApiMethodTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

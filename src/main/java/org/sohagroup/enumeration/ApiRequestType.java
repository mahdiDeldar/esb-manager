package org.sohagroup.enumeration;

public enum ApiRequestType {
    REST("rest"),
    GRAPHQL("graphql"),
    SOCKET("socket");
    private final String value;

    ApiRequestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

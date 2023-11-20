package org.sohagroup.enumeration;

public enum NodeAttributeCategory {
    body("REQUEST_BODY"),
    param("REQUEST_PARAM"),
    header("REQUEST_HEADER");
    private final String value;

    NodeAttributeCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

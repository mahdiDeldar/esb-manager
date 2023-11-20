package org.sohagroup.enumeration;

public enum NodeAttributeType {
    response("RESPONSE"),
    request("REQUEST");
    private final String value;

    NodeAttributeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package org.sohagroup.enumeration;

public enum NodeResponseType {
    List("List"),
    Single("Single");
    private final String value;

    NodeResponseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

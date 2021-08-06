package com.hyd.pexporter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExportItem {

    public static final String TYPE_GAUGE = "gauge";

    public static final String TYPE_COUNTER = "counter";

    private String name;

    private Map<String, String> attributes = new HashMap<>();

    private String value;

    private String help;

    /**
     * {@link #TYPE_COUNTER} or {@link #TYPE_GAUGE}
     */
    private String type;

    public ExportItem() {
    }

    public ExportItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public ExportItem setAttribute(String name, String value) {
        this.attributes.put(name, value);
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toCacheKey() {
        return this.name + ":" + this.attributes;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toExportLine() {
        if (this.value == null) {
            return null;
        }

        String attributeString = "";
        if (!this.attributes.isEmpty()) {
            attributeString = "{" + this.attributes
                .entrySet().stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining(",")) + "}";
        }

        String helpString = help == null ? "" : ("# HELP " + name + " " + help + "\n");
        String typeString = type == null ? "" : ("# TYPE " + name + " " + type + "\n");

        return helpString + typeString + this.name + attributeString + " " + this.value;
    }
}

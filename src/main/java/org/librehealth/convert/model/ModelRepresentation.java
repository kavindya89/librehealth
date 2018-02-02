package org.librehealth.convert.model;

import org.json.simple.JSONObject;

public class ModelRepresentation {
    private String display;
    private String id;
    private String value;
    private String system;
    private JSONObject representation;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject getRepresentation() {
        return representation;
    }

    public void setRepresentation(JSONObject representation) {
        this.representation = representation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}

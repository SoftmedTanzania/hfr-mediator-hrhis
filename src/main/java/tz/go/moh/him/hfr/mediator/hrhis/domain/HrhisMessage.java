package tz.go.moh.him.hfr.mediator.hrhis.domain;

import java.util.List;
import java.util.Map;

/**
 * Represents a Health Facility Registry request message.
 */
public class HrhisMessage {
    private String name;
    private String code;
    private String shortName;
    private String coordinates;
    private boolean active;
    private Map<String, Object> parent;
    private List<Map<String, Object>> organisationUnitGroups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, Object> getParent() {
        return parent;
    }

    public void setParent(Map<String, Object> parent) {
        this.parent = parent;
    }

    public List<Map<String, Object>> getOrganisationUnitGroups() {
        return organisationUnitGroups;
    }

    public void setOrganisationUnitGroups(List<Map<String, Object>> organisationUnitGroups) {
        this.organisationUnitGroups = organisationUnitGroups;
    }
}

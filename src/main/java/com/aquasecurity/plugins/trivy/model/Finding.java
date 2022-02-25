package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.file.FileSystems;
import java.util.List;


public class Finding {

    @JsonProperty("rule_id")
    public String RuleID;

    @JsonProperty("long_id")
    public String LongID;

    @JsonProperty("rule_description")
    public String RuleDescription;

    @JsonProperty("rule_provider")
    public String Provider;

    @JsonProperty("rule_service")
    public String Service;

    @JsonProperty("impact")
    public String Impact;

    @JsonProperty("resolution")
    public String Resolution;

    @JsonProperty("links")
    public List<String> Links;

    @JsonProperty("description")
    public String Description;

    @JsonProperty("severity")
    public Severity Severity;

    @JsonProperty("status")
    public int Status;

    @JsonProperty("resource")
    public String Resource;

    @JsonProperty("location")
    public Location Location;

    private String basePath;


    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = String.format("%s%s", basePath, FileSystems.getDefault().getSeparator());
    }

    public String getRelativePath() {
        return Location.Filename.replace(basePath, "");
    }
}

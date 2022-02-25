package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Result {
    @JsonProperty("Target")
    public String target;
    @JsonProperty("Class")
    public String ClassName;
    @JsonProperty("Type")
    public String type;
    @JsonProperty("Vulnerabilities")
    public List<Vulnerability> vulnerabilities;
    @JsonProperty("MisconfSummary")
    public MisconfSummary misconfSummary;
    @JsonProperty("Misconfigurations")
    public List<Misconfiguration> misconfigurations;
}

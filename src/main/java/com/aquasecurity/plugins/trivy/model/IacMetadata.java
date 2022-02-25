package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IacMetadata {
    @JsonProperty("Resource")
    public String resource;
    @JsonProperty("Provider")
    public String provider;
    @JsonProperty("Service")
    public String service;
    @JsonProperty("StartLine")
    public int startLine;
    @JsonProperty("EndLine")
    public int endLine;
}

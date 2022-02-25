package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Misconfiguration {
    @JsonProperty("Type")
    public String type;
    @JsonProperty("ID")
    public String id;
    @JsonProperty("Title")
    public String title;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("Message")
    public String message;
    @JsonProperty("Namespace")
    public String namespace;
    @JsonProperty("Query")
    public String query;
    @JsonProperty("Resolution")
    public String resolution;
    @JsonProperty("Severity")
    public String severity;
    @JsonProperty("PrimaryURL")
    public String primaryURL;
    @JsonProperty("References")
    public List<String> references;
    @JsonProperty("Status")
    public String status;
    @JsonProperty("Layer")
    public Layer layer;
    @JsonProperty("IacMetadata")
    public IacMetadata iacMetadata;
}

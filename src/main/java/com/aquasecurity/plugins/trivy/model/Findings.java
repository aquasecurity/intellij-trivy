package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Findings {

    @JsonProperty("SchemaVersion")
    public int schemaVersion;
    @JsonProperty("ArtifactName")
    public String artifactName;
    @JsonProperty("ArtifactType")
    public String artifactType;
    @JsonProperty("Metadata")
    public Metadata metadata;
    @JsonProperty("Results")
    public List<Result> results;

    public List<Finding> getBySeverity(Severity severity) {
        return null;
    }

    public void setBasePath(String basePath) {
    }
}




package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MisconfSummary {
    @JsonProperty("Successes")
    public int successes;
    @JsonProperty("Failures")
    public int failures;
    @JsonProperty("Exceptions")
    public int exceptions;
}

package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Redhat {
    @JsonProperty("V3Vector")
    public String v3Vector;
    @JsonProperty("V3Score")
    public double v3Score;
}

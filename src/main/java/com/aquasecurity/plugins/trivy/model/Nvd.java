package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Nvd {
    @JsonProperty("V2Vector")
    public String v2Vector;
    @JsonProperty("V3Vector")
    public String v3Vector;
    @JsonProperty("V2Score")
    public double v2Score;
    @JsonProperty("V3Score")
    public double v3Score;
}

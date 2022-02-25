package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {
    @JsonProperty("ImageConfig")
    public ImageConfig imageConfig;
}

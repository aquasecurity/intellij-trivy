package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataSource {
    @JsonProperty("ID")
    public String iD;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("URL")
    public String uRL;
}

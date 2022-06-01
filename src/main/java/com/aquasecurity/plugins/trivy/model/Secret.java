package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Secret{
    @JsonProperty("RuleID")
    public String ruleID;
    @JsonProperty("Category")
    public String category;
    @JsonProperty("Severity")
    public String severity;
    @JsonProperty("Title")
    public String title;
    @JsonProperty("StartLine")
    public int startLine;
    @JsonProperty("EndLine")
    public int endLine;
    @JsonProperty("Match")
    public String match;
}
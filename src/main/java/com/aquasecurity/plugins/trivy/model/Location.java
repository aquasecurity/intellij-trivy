package com.aquasecurity.plugins.trivy.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

    public String Filename;
    public int StartLine;
    public int EndLine;

    @JsonCreator
    public Location(
            @JsonProperty("filename") String filename,
            @JsonProperty("start_line") int startLine,
            @JsonProperty("end_line") int endLine
    ) {
        this.Filename = filename;
        this.StartLine = startLine;
        this.EndLine = endLine;
    }

}

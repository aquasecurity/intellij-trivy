package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

data class Location(
    @JsonProperty("Filename") var filename: String?,
    @JsonProperty("StartLine") val startLine: Int?,
    @JsonProperty("EndLine") val endLine: Int?,
)

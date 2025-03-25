package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonProperty

data class Location(
    @JsonProperty("StartLine") val startLine: Long,
    @JsonProperty("EndLine") val endLine: Long,
)

package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ControlResult
@JsonCreator
constructor(
    @JsonProperty("reason") val reason: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("matched_data") val matchedData: String,
    @JsonProperty("location") val location: String,
    @JsonProperty("target_line") val targetLine: Int?
)

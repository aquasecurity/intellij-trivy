package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class VendorScoring
@JsonCreator
constructor(
    @JsonProperty("vendorName") val vendorName: String,
    @JsonProperty("V3Score") val v3Score: Double,
    @JsonProperty("V3Vector") val v3Vector: String,
    @JsonProperty("Severity") val severity: Long,
    @JsonProperty("V2Score") val v2Score: Double?,
    @JsonProperty("V2Vector") val v2Vector: String?,
)

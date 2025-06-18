package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

data class Bitnami(
    @JsonProperty("V3Vector") val v3Vector: String,
    @JsonProperty("V3Score") val v3Score: Double,
)

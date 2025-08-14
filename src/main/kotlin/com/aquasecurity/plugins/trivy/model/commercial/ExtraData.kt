package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonProperty

data class ExtraData(
    @JsonProperty("References") val references: List<String>?,
    @JsonProperty("Direct") val direct: String?,
    @JsonProperty("PackageRoots") val packageRoots: List<String>?,
    @JsonProperty("Fix") val fix: Fix?,
)

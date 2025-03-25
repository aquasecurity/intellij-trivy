package com.aquasecurity.plugins.trivy.model.oss

import com.fasterxml.jackson.annotation.JsonProperty

data class Custom(
    @JsonProperty("indirect") val indirect: Boolean,
    @JsonProperty("pkgRoots") val pkgRoots: List<String>?,
)

package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonProperty

data class Fix(
    @JsonProperty("Resolution") val resolution: String?,
)

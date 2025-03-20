package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonProperty

data class Code(
    @JsonProperty("Lines") val lines: List<Line>?,
)

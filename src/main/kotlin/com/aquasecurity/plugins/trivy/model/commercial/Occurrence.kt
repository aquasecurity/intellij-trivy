package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonProperty

data class Occurrence(
  @JsonProperty("Resource") val resource: String,
  @JsonProperty("Filename") val filename: String,
  @JsonProperty("Location") val location: Location,
)

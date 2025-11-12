package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

data class Custom(
  @JsonProperty("indirect") val indirect: Boolean,
  @JsonProperty("pkgRoots") val pkgRoots: List<String>?,
)

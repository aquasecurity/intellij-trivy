package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonProperty

data class Line(
  @JsonProperty("Number") val number: Long,
  @JsonProperty("Content") val content: String,
  @JsonProperty("IsCause") val isCause: Boolean,
  @JsonProperty("Annotation") val annotation: String,
  @JsonProperty("Truncated") val truncated: Boolean,
  @JsonProperty("Highlighted") val highlighted: String?,
  @JsonProperty("FirstCause") val firstCause: Boolean,
  @JsonProperty("LastCause") val lastCause: Boolean,
)

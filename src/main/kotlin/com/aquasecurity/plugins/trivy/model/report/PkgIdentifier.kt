package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

data class PkgIdentifier(
  @JsonProperty("PURL") val purl: String,
  @JsonProperty("UID") val uid: String,
)

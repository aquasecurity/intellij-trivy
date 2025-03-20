package com.aquasecurity.plugins.trivy.model.oss

import com.fasterxml.jackson.annotation.JsonProperty

data class PkgIdentifier(
    @JsonProperty("PURL") val purl: String,
    @JsonProperty("UID") val uid: String,
)

package com.aquasecurity.plugins.trivy.model.oss

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Package
@JsonCreator
constructor(
    @JsonProperty("ID") val id: String?,
    @JsonProperty("Name") val name: String,
    @JsonProperty("Identifier") val identifier: Identifier,
    @JsonProperty("Version") val version: String?,
    @JsonProperty("DependsOn") val dependsOn: List<String>?,
    @JsonProperty("Layer") val layer: Map<String, Any>,
    @JsonProperty("Locations") val locations: List<Location>?,
    @JsonProperty("Licenses") val licenses: List<String>?,
)

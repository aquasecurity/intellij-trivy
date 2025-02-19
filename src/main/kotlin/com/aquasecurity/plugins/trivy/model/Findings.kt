package com.aquasecurity.plugins.trivy.model

import com.fasterxml.jackson.annotation.JsonProperty

class Findings {
    @JsonProperty("SchemaVersion")
    var schemaVersion: Int = 0

    @JsonProperty("ArtifactName")
    var artifactName: String? = null

    @JsonProperty("ArtifactType")
    var artifactType: String? = null

    @JsonProperty("Metadata")
    var metadata: Metadata? = null

    @JsonProperty("Results")
    var results: List<Result>? = null

    fun getBySeverity(severity: Severity?): List<Finding>? {
        return null
    }

    fun setBasePath(basePath: String?) {
    }
}
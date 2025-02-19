package com.aquasecurity.plugins.trivy.model

import com.fasterxml.jackson.annotation.JsonProperty

class IacMetadata {
    @JsonProperty("Resource")
    var resource: String? = null

    @JsonProperty("Provider")
    var provider: String? = null

    @JsonProperty("Service")
    var service: String? = null

    @JsonProperty("StartLine")
    var startLine: Int = 0

    @JsonProperty("EndLine")
    var endLine: Int = 0
}
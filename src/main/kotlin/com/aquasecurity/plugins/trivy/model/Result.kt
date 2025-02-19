package com.aquasecurity.plugins.trivy.model

import com.fasterxml.jackson.annotation.JsonProperty

class Result {
    @JsonProperty("Target")
    var target: String? = null

    @JsonProperty("Class")
    var ClassName: String? = null

    @JsonProperty("Type")
    var type: String? = null

    @JsonProperty("Vulnerabilities")
    var vulnerabilities: List<Vulnerability>? = null

    @JsonProperty("MisconfSummary")
    var misconfSummary: MisconfSummary? = null

    @JsonProperty("Misconfigurations")
    var misconfigurations: List<Misconfiguration>? = null

    @JsonProperty("Secrets")
    var secrets: List<Secret>? = null
}
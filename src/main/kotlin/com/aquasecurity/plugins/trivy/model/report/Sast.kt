package com.aquasecurity.plugins.trivy.model.report

import com.aquasecurity.plugins.trivy.model.commercial.Fix
import com.fasterxml.jackson.annotation.JsonProperty

class Sast {
    @JsonProperty("CheckID")
    var checkID: String? = null
    @JsonProperty("Title")
    var title: String? = null
    @JsonProperty("CWE")
    var cwe: String? = null
    @JsonProperty("Message")
    var message: String? = null
    @JsonProperty("Category")
    var category: String? = null
    @JsonProperty("OWASP")
    var owasp: List<String>? = null
    @JsonProperty("Technologies")
    var technologies: List<String>? = null
    @JsonProperty("Fix")
    var fix: Fix? = null
    @JsonProperty("Severity")
    var severity: String? = null
    @JsonProperty("Confidence")
    var confidence: String? = null
    @JsonProperty("Likelihood")
    var likelihood: String? = null
    @JsonProperty("Impact")
    var impact: String? = null
    @JsonProperty("References")
    var references: List<String>? = null
    @JsonProperty("StartLine")
    var startLine: Int = 0
    @JsonProperty("EndLine")
    var endLine: Int = 0
    @JsonProperty("Remediation")
    var remediation: String? = null
}

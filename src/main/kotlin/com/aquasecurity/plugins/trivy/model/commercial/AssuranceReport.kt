package com.aquasecurity.plugins.trivy.model.commercial

import com.aquasecurity.plugins.trivy.model.oss.Report
import com.fasterxml.jackson.annotation.JsonProperty

data class AssuranceReport(
    @JsonProperty("Report") val report: Report,
    @JsonProperty("Results") val results: List<Result>,
)

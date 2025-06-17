package com.aquasecurity.plugins.trivy.model.commercial

import com.aquasecurity.plugins.trivy.model.report.Report
import com.fasterxml.jackson.annotation.JsonProperty

data class AssuranceReport(
    @JsonProperty("Report") var report: Report,
    @JsonProperty("Results") val results: List<Result>,
)

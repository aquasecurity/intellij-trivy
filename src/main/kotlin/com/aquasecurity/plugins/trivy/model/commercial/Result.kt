package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonProperty

data class Result(
    @JsonProperty("AVDID") val avdid: String,
    @JsonProperty("Message") val message: String,
    @JsonProperty("Type") val type: Long,
    @JsonProperty("Severity") val severity: Long,
    @JsonProperty("Title") val title: String,
    @JsonProperty("Filename") val filename: String,
    @JsonProperty("PolicyResults") val policyResults: List<PolicyResult>?,
    @JsonProperty("PkgName") val pkgName: String?,
    @JsonProperty("InstalledVersion") val installedVersion: String?,
    @JsonProperty("FixedVersion") val fixedVersion: String?,
    @JsonProperty("DataSource") val dataSource: String?,
    @JsonProperty("VendorScoring") val vendorScoring: List<VendorScoring>?,
    @JsonProperty("PublishedDate") val publishedDate: Long?,
    @JsonProperty("LastModified") val lastModified: Long?,
    @JsonProperty("ExtraData") val extraData: ExtraData?,
    @JsonProperty("Resource") val resource: String?,
    @JsonProperty("StartLine") val startLine: Long?,
    @JsonProperty("EndLine") val endLine: Long?,
)

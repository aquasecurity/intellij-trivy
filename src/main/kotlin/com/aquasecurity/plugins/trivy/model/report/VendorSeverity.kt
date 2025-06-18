package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

data class VendorSeverity(
    @JsonProperty("alma") val alma: Long?,
    @JsonProperty("amazon") val amazon: Long?,
    @JsonProperty("cbl-mariner") val cblMariner: Long?,
    @JsonProperty("ghsa") val ghsa: Long,
    @JsonProperty("nvd") val nvd: Long?,
    @JsonProperty("oracle-oval") val oracleOval: Long?,
    @JsonProperty("photon") val photon: Long?,
    @JsonProperty("redhat") val redhat: Long?,
    @JsonProperty("rocky") val rocky: Long?,
    @JsonProperty("azure") val azure: Long?,
    @JsonProperty("ubuntu") val ubuntu: Long?,
    @JsonProperty("bitnami") val bitnami: Long?,
    @JsonProperty("nodejs-security-wg") val nodejsSecurityWg: Long?,
    @JsonProperty("ruby-advisory-db") val rubyAdvisoryDb: Long?,
)

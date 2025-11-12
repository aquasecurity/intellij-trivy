package com.aquasecurity.plugins.trivy.model.commercial

import com.fasterxml.jackson.annotation.JsonProperty

data class PolicyResult(
  @JsonProperty("PolicyID") val policyId: String,
  @JsonProperty("Failed") val failed: Boolean?,
  @JsonProperty("Reason") val reason: String?,
  @JsonProperty("Controls") val controls: List<String>?,
  @JsonProperty("ControlResult") val controlResult: List<ControlResult>?,
  @JsonProperty("policy_name") val policyName: String,
)

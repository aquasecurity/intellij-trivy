package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

class Secret {
  @JsonProperty("RuleID") var ruleID: String? = null

  @JsonProperty("Category") var category: String? = null

  @JsonProperty("Severity") var severity: String? = null

  @JsonProperty("Title") var title: String? = null

  @JsonProperty("StartLine") var startLine: Int = 0

  @JsonProperty("EndLine") var endLine: Int = 0

  @JsonProperty("Match") var match: String? = null
}

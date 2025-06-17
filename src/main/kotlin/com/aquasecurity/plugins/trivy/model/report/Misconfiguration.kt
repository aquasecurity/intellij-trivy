package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

class Misconfiguration {
  @JsonProperty("Type") var type: String? = null

  @JsonProperty("ID") var id: String? = null

  @JsonProperty("Title") var title: String? = null

  @JsonProperty("Description") var description: String? = null

  @JsonProperty("Message") var message: String? = null

  @JsonProperty("Namespace") var namespace: String? = null

  @JsonProperty("Query") var query: String? = null

  @JsonProperty("Resolution") var resolution: String? = null

  @JsonProperty("Severity") var severity: String? = null

  @JsonProperty("PrimaryURL") var primaryURL: String? = null

  @JsonProperty("References") var references: List<String>? = null

  @JsonProperty("Status") var status: String? = null

  @JsonProperty("Layer") var layer: Layer? = null

  @JsonProperty("IacMetadata") var iacMetadata: IacMetadata? = null

  @JsonProperty("CauseMetadata") var causeMetadata: IacMetadata? = null
}

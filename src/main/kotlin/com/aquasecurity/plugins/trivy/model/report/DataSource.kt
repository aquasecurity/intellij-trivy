package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

class DataSource {
  @JsonProperty("ID") var iD: String? = null

  @JsonProperty("Name") var name: String? = null

  @JsonProperty("URL") var uRL: String? = null
}

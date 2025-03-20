package com.aquasecurity.plugins.trivy.model.oss

import com.fasterxml.jackson.annotation.JsonProperty

class Redhat {
  @JsonProperty("V2Vector") var v2Vector: String? = null

  @JsonProperty("V3Vector") var v3Vector: String? = null

  @JsonProperty("V2Score") var v2Score: Double = 0.0

  @JsonProperty("V3Score") var v3Score: Double = 0.0
}

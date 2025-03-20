package com.aquasecurity.plugins.trivy.model.oss

import com.fasterxml.jackson.annotation.JsonProperty

class MisconfSummary {
  @JsonProperty("Successes") var successes: Int = 0

  @JsonProperty("Failures") var failures: Int = 0

  @JsonProperty("Exceptions") var exceptions: Int = 0
}

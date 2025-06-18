package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

class CVSS {
  @JsonProperty("nvd") var nvd: Nvd? = null

  @JsonProperty("redhat") var redhat: Redhat? = null
}

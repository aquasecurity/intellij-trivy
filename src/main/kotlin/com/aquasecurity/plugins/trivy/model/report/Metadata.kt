package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

class Metadata {
  @JsonProperty("ImageConfig") var imageConfig: ImageConfig? = null
}

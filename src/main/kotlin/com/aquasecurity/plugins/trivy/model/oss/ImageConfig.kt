package com.aquasecurity.plugins.trivy.model.oss

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class ImageConfig {
  @JsonProperty("architecture") var architecture: String? = null

  @JsonProperty("created") var created: Date? = null

  @JsonProperty("os") var os: String? = null

  @JsonProperty("rootfs") var rootfs: Rootfs? = null

  @JsonProperty("config") var config: Config? = null
}

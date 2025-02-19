package com.aquasecurity.plugins.trivy.model

import java.util.*

class ImageConfig {
    var architecture: String? = null
    var created: Date? = null
    var os: String? = null
    var rootfs: Rootfs? = null
    var config: Config? = null
}
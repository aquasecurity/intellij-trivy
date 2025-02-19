package com.aquasecurity.plugins.trivy.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.nio.file.FileSystems

class Finding {
    @JsonProperty("rule_id")
    var RuleID: String? = null

    @JsonProperty("long_id")
    var LongID: String? = null

    @JsonProperty("rule_description")
    var RuleDescription: String? = null

    @JsonProperty("rule_provider")
    var Provider: String? = null

    @JsonProperty("rule_service")
    var Service: String? = null

    @JsonProperty("impact")
    var Impact: String? = null

    @JsonProperty("resolution")
    var Resolution: String? = null

    @JsonProperty("links")
    var Links: List<String>? = null

    @JsonProperty("description")
    var Description: String? = null

    @JsonProperty("severity")
    var Severity: Severity? = null

    @JsonProperty("status")
    var Status: Int = 0

    @JsonProperty("resource")
    var Resource: String? = null

    @JsonProperty("location")
    var Location: Location? = null

    private var basePath: String? = null


    fun getBasePath(): String? {
        return basePath
    }

    fun setBasePath(basePath: String?) {
        this.basePath = String.format("%s%s", basePath, FileSystems.getDefault().separator)
    }

    val relativePath: String
        get() = Location!!.Filename.replace(basePath!!, "")
}
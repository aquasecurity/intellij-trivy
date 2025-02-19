package com.aquasecurity.plugins.trivy.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class Location @JsonCreator constructor(
    @param:JsonProperty("filename") var Filename: String,
    @param:JsonProperty("start_line") var StartLine: Int,
    @param:JsonProperty("end_line") var EndLine: Int
)
package com.aquasecurity.plugins.trivy.model.oss

import com.fasterxml.jackson.annotation.JsonProperty

data class Report(
    @JsonProperty("SchemaVersion") val schemaVersion: Long,
    @JsonProperty("CreatedAt") val createdAt: String,
    @JsonProperty("ArtifactName") val artifactName: String,
    @JsonProperty("ArtifactType") val artifactType: String,
    @JsonProperty("Metadata") val metadata: Metadata,
    @JsonProperty("Results") val results: List<Result>?,
) {

  fun findMatchingResult(filepath: String, matchId: String): List<Any?> {

    var returnResults: List<Any?> = listOf()

      if (results == null) {
          return returnResults
      }

    val fileResults = results.filter { r -> r.target == filepath }

    if (fileResults.isEmpty()) {
      return returnResults
    }

    for (result in fileResults) {
      result.findMatchingIssue(matchId).forEach { match ->
        if (match != null) {
          returnResults += match
        }
      }
    }

    return returnResults
  }
}

package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty

class Result {
  @JsonProperty("Target") var target: String? = null

  @JsonProperty("Class") var ClassName: String? = null

  @JsonProperty("Type") var type: String? = null

  @JsonProperty("Vulnerabilities") var vulnerabilities: List<Vulnerability>? = null

  @JsonProperty("Packages") var packages: List<Package>? = null

  @JsonProperty("MisconfSummary") var misconfSummary: MisconfSummary? = null

  @JsonProperty("Misconfigurations") var misconfigurations: List<Misconfiguration>? = null

  @JsonProperty("Secrets") var secrets: List<Secret>? = null

  @JsonProperty("Sast") var sasts: List<Sast>? = null

  private var consolidatedResults: List<Any?> = listOf()

  private fun groupConsolidatedResults(): List<Any?> {
    if (!consolidatedResults.isEmpty()) {
      return consolidatedResults
    }

    vulnerabilities?.forEach { consolidatedResults += it }
    misconfigurations?.forEach { consolidatedResults += it }
    secrets?.forEach { consolidatedResults += it }
    sasts?.forEach { consolidatedResults += it }

    consolidatedResults =
        this.consolidatedResults.sortedBy { it ->
          when (it) {
            is Vulnerability -> {
              it.severity
            }

            is Misconfiguration -> {
              it.severity
            }

            is Secret -> {
              it.severity
            }

            is Sast -> {
              it.severity
            }

            else -> {
              ""
            }
          }
        }

    return consolidatedResults
  }

  fun findMatchingIssue(issueId: String): List<Any?> {
    var returnResults: List<Any?> = listOf()
    for (issue in groupConsolidatedResults()) {
      when (issue) {
        is Vulnerability -> {
          if (issue.vulnerabilityId == issueId) {
            returnResults += issue
          }
        }

        is Misconfiguration -> {
          if (issue.id == issueId) {
            returnResults += issue
          }
        }

        is Secret -> {
          if (issue.title == issueId) {
            returnResults += issue
          }
        }
        is Sast -> {
          if (issue.checkID == issueId) {
            returnResults += issue
          }
        }

        else -> {
          // Handle other types if necessary
        }
      }
    }

    return returnResults
  }
}

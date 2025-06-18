package com.aquasecurity.plugins.trivy.model.report

import com.fasterxml.jackson.annotation.JsonProperty
import java.nio.file.Paths

data class Report(
    @JsonProperty("SchemaVersion") val schemaVersion: Long,
    @JsonProperty("CreatedAt") val createdAt: String,
    @JsonProperty("ArtifactName") val artifactName: String,
    @JsonProperty("ArtifactType") val artifactType: String,
    @JsonProperty("Metadata") val metadata: Metadata,
    @JsonProperty("Results") val results: List<Result>?,
) {
  val project = com.intellij.openapi.project.ProjectManager.getInstance().openProjects.firstOrNull()

  fun findMatchingResult(filepath: String, matchId: String): List<Any?> {

    var returnResults: List<Any?> = listOf()

    if (results == null) {
      return returnResults
    }

    fun isMatchingFile(result: Result, filepath: String): Boolean {
      val projectRoot = project!!.basePath // or project.getBasePath()
      val targetPath = result.target
      // if the target is a relative path, it should be relative to the project root
      if (!Paths.get(targetPath).isAbsolute()) {
        return targetPath == filepath
      }

      try {
        val relativePath =
            if (projectRoot != null) {
              Paths.get(projectRoot).relativize(Paths.get(targetPath)).toString()
            } else {
              targetPath // fallback if project root is not available
            }

        return relativePath == filepath
      } catch (e: Exception) {
        // Handle any exceptions that may occur during path manipulation
        return false
      }
    }

    val fileResults = results.filter { r -> isMatchingFile(r, filepath) }

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

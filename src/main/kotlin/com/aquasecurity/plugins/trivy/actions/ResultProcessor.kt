package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.model.commercial.AssuranceReport
import com.aquasecurity.plugins.trivy.model.report.Report
import com.aquasecurity.plugins.trivy.settings.TrivyProjectSettingState
import com.aquasecurity.plugins.trivy.ui.TrivyWindow
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.StreamReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.intellij.openapi.project.Project
import java.io.File
import java.io.IOException

object ResultProcessor {

  fun handleFailure(trivyWindow: TrivyWindow) {
    trivyWindow.redraw()
  }

  fun updateResults(project: Project, resultFile: File?, trivyWindow: TrivyWindow) {
    try {
      if (resultFile == null || !resultFile.exists()) {
        TrivyNotificationGroup.notifyError(project, "Failed to find the results file.")
        return
      }

      // check if the results file is empty
      if (resultFile.length() == 0L) {
        TrivyNotificationGroup.notifyError(project, "The results file is empty.")
        return
      }

      val projectSettings = TrivyProjectSettingState.getInstance(project)
      val report = processReport(project, resultFile)
      updatePackageLocations(report)
      trivyWindow.updateFindings(report)
      if (projectSettings.useAquaPlatform) {
        // For Aqua Platform, we need to process the report differently
        val assuranceResultFile = resultFile.absolutePath.replace(".json", "_assurance.json")
        if (File(assuranceResultFile).exists()) {
          val assuranceReport = getAssuranceReport(project, File(assuranceResultFile), report)
          trivyWindow.updateAssuranceResults(assuranceReport)
        }
      }
    } catch (e: IOException) {
      TrivyNotificationGroup.notifyError(
        project,
        "Failed to process the results file. ${e.localizedMessage}",
      )
    } catch (e: Exception) {
      TrivyNotificationGroup.notifyError(
        project,
        "An unexpected error occurred while processing the results. ${e.localizedMessage}",
      )
    } finally {
      trivyWindow.redraw()
    }
  }

  private fun updatePackageLocations(report: Report) {
    if (report.results == null) {
      return
    }

    report.results.forEach { result ->
      result.vulnerabilities?.forEach { vuln ->
        result.packages?.forEach { pkg ->
          if (
            pkg.name == vuln.pkgName &&
              pkg.version == vuln.installedVersion &&
              !pkg.locations.isNullOrEmpty()
          ) {
            vuln.location = pkg.locations[0]
          }
        }
      }
    }
  }

  private fun processReport(project: Project, resultFile: File): Report {
    return try {
      val jsonFactory =
        JsonFactory.builder().enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION).build()
      val findingsMapper =
        ObjectMapper(jsonFactory).apply {
          disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
          disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          disable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
        }

      // Register Kotlin module and configure Kotlin-specific features so Jackson will
      // be lenient with nulls for Kotlin types (treat null collections/maps as empty,
      // treat null as default, and disable strict null checks which otherwise fail
      // when JSON contains null for non-null Kotlin constructor parameters).
      val kotlinModule =
        KotlinModule.Builder()
          .configure(KotlinFeature.NullToEmptyCollection, true)
          .configure(KotlinFeature.NullToEmptyMap, true)
          .configure(KotlinFeature.NullIsSameAsDefault, true)
          .build()
      findingsMapper.registerModule(kotlinModule)

      findingsMapper.readValue(resultFile, Report::class.java)
    } catch (e: IOException) {
      TrivyNotificationGroup.notifyError(
        project,
        "Failed to deserialize the results file. ${e.localizedMessage}",
      )
      throw e
    }
  }

  private fun getAssuranceReport(
    project: Project,
    resultFile: File,
    report: Report,
  ): AssuranceReport {
    return try {
      val jsonFactory =
        JsonFactory.builder().enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION).build()
      val findingsMapper =
        ObjectMapper(jsonFactory).apply {
          disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
          disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }

      // Same Kotlin module configuration for assurance reports
      val kotlinModule =
        KotlinModule.Builder()
          .configure(KotlinFeature.NullToEmptyCollection, true)
          .configure(KotlinFeature.NullToEmptyMap, true)
          .configure(KotlinFeature.NullIsSameAsDefault, true)
          .build()
      findingsMapper.registerModule(kotlinModule)

      val rep = findingsMapper.readValue(resultFile, AssuranceReport::class.java)
      AssuranceReport(report, rep.results)
    } catch (e: IOException) {
      TrivyNotificationGroup.notifyError(
        project,
        "Failed to deserialize the results file. ${e.localizedMessage}",
      )
      throw e
    }
  }
}

package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.icons.TrivyIcons
import com.aquasecurity.plugins.trivy.model.commercial.Result
import com.aquasecurity.plugins.trivy.model.oss.Location
import com.aquasecurity.plugins.trivy.model.oss.Misconfiguration
import com.aquasecurity.plugins.trivy.model.oss.Secret
import com.aquasecurity.plugins.trivy.model.oss.Vulnerability
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

class LocationTreeNode(
    private val filepath: String,
    private val filetype: String,
    val finding: Any,
    val child: Boolean = false
) : DefaultMutableTreeNode(), TrivyTreeNode {
  private var fileLocation: Location
  private var locationTitle: String
  private var severity: String

  private var helperObject: Result? = null

  init {
    require(
        finding is Misconfiguration ||
            finding is Vulnerability ||
            finding is Secret ||
            finding is Result)

    this.fileLocation = Location(filepath, 1, 1)
    this.locationTitle = filepath
    this.severity = "UNKNOWN"

    when {
      finding is Result -> handleCommercialResult(finding)
      finding is Misconfiguration -> handleMisconfiguration(finding)
      finding is Vulnerability -> handleVulnerability(finding)
      finding is Secret -> handleSecret(finding)
    }
  }

  val location: Location
    get() = this.fileLocation

  private fun handleSecret(secret: Secret) {
    this.fileLocation = Location(this.filepath, secret.startLine, secret.endLine)

    this.locationTitle = secret.title.toString()
    this.severity = secret.severity.toString()
  }

  private fun handleVulnerability(vuln: Vulnerability) {
    if (vuln.location == null) {
      this.fileLocation = Location(filepath, 1, 1)
    } else {
      this.fileLocation = Location(filepath, vuln.location!!.startLine, vuln.location!!.endLine)
    }
    this.locationTitle = vuln.vulnerabilityId
    this.severity = vuln.severity
  }

  private fun handleCommercialResult(cr: Result) {
    if (cr.startLine != null && cr.endLine != null) {
      this.fileLocation = Location(filepath, cr.startLine.toInt(), cr.endLine.toInt())
    } else {
      this.locationTitle = cr.title
      this.fileLocation = Location(filepath, 1, 1)
    }

    this.severity = cr.severity.toString()
  }

  private fun handleMisconfiguration(finding: Misconfiguration) {
    if (finding.causeMetadata != null) {
      this.fileLocation =
          Location(filepath, finding.causeMetadata!!.startLine, finding.causeMetadata!!.endLine)
    } else if (finding.iacMetadata != null) {
      this.fileLocation =
          Location(filepath, finding.iacMetadata!!.startLine, finding.iacMetadata!!.endLine)
    }
    this.severity = finding.severity!!
    this.locationTitle = finding.title!!
    if (this.child) {
      if (this.fileLocation.startLine != null &&
          this.fileLocation.startLine != this.fileLocation.endLine) {
        this.locationTitle =
            "${this.filepath}:[${this.fileLocation.startLine}-${this.fileLocation.endLine}]"
      } else {
        this.locationTitle = this.filepath
      }
    }
  }

  fun setHelperObject(helperObject: Result) {
    this.helperObject = helperObject
  }

  fun getHelpObject(): Any? {
    if (this.helperObject != null) {
      return this.helperObject
    }
    return this.finding
  }

  override val icon: Icon?
    get() {
      if (this.finding is Secret) {
        return TrivyIcons.Secret
      }

      var icon = TrivyIcons.Unidentified
      when (severity) {
        "CRITICAL" -> icon = TrivyIcons.Critical
        "HIGH" -> icon = TrivyIcons.High
        "MEDIUM" -> icon = TrivyIcons.Medium
        "LOW" -> icon = TrivyIcons.Low
        "1" -> icon = TrivyIcons.Critical
        "2" -> icon = TrivyIcons.High
        "3" -> icon = TrivyIcons.Medium
        "4" -> icon = TrivyIcons.Low
      }
      return icon
    }

  override val title: String?
    get() = this.locationTitle

  override val tooltip: String?
    get() = ""
}

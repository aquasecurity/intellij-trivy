package com.aquasecurity.plugins.trivy.ui

import com.aquasecurity.plugins.trivy.model.commercial.Result
import com.aquasecurity.plugins.trivy.model.oss.Misconfiguration
import com.aquasecurity.plugins.trivy.model.oss.Secret
import com.aquasecurity.plugins.trivy.model.oss.Vulnerability
import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import org.jdesktop.swingx.JXHyperlink
import java.awt.Font
import java.net.URI
import java.util.function.Consumer
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel

class FindingsHelper : ScrollablePanel() {
  private var filepath: String? = null
  private var vulnerability: Vulnerability? = null
  private var misconfig: Misconfiguration? = null
  private var secret: Secret? = null
  private var commercialResult: Result? = null

  init {
    layout = BoxLayout(this, BoxLayout.Y_AXIS)
    border = JBUI.Borders.empty(10)
    background = JBColor.PanelBackground
  }

  private fun updateHelp() {
    removeAll()
    if (this.misconfig != null) {
      addHelpSection(misconfig!!.title!!, misconfig?.description)
      addHelpSection("ID", misconfig?.id)
      addHelpSection("Severity", convertSeverity(misconfig?.severity.toString()))
      addHelpSection("Resolution", misconfig?.resolution)
      addHelpSection("Filename", filepath)

      if (misconfig?.references!!.isNotEmpty()) {
        addLinkSection(misconfig?.references!!)
      }
    }

    if (this.vulnerability != null) {
      addHelpSection("", vulnerability?.vulnerabilityId)
      addHelpSection(vulnerability?.title!!, vulnerability?.description)
      addHelpSection("Severity", convertSeverity(vulnerability?.severity.toString()))
      addHelpSection("Package Name", vulnerability?.pkgName)
      addHelpSection("Installed Version", vulnerability?.installedVersion)
      addHelpSection("Fixed Version", vulnerability?.fixedVersion)
      addHelpSection("Filename", this.filepath)

      if (vulnerability?.references!!.isNotEmpty()) {
        addLinkSection(vulnerability?.references!!)
      }
    }

    if (this.secret != null) {
      addHelpSection("Secret Leak", secret?.title)
      addHelpSection("Severity", convertSeverity(secret?.severity.toString()))
      addHelpSection("Match", secret?.match)
      addHelpSection("Filename", filepath)
    }

    if (this.commercialResult != null) {
      var fix = commercialResult?.fixedVersion
      if (commercialResult?.extraData?.fix != null) {
        fix = commercialResult?.extraData?.fix?.toString()
        if (fix!!.endsWith(")")) {
          fix = fix.substring(0, fix.length - 1).replace("Fix(resolution=", "")
        }
      }

      addHelpSection("", commercialResult?.title)
      addHelpSection("", commercialResult?.avdid)
      addHelpSection("", commercialResult?.message)
      addHelpSection("Severity", convertSeverity(commercialResult?.severity.toString()))
      addHelpSection("Fix", fix)
      addHelpSection("Filename", filepath)
      addLinkSection(commercialResult?.extraData?.references?.toList()!!)
    }
  }

  private fun addLinkSection(links: List<String>) {
    val section = JPanel()
    section.border = JBUI.Borders.empty(10)

    section.layout = BoxLayout(section, BoxLayout.Y_AXIS)

    val font = UIUtil.getLabelFont()
    val headingFont = font.deriveFont(font.style or Font.BOLD)
    val heading = JLabel()
    heading.font = headingFont
    heading.text = "Links"
    section.add(heading)

    links.forEach(
        Consumer { link: String? ->
          val hyperlink = JXHyperlink()
          hyperlink.text = String.format("<html>%s</html>", link)
          hyperlink.setURI(URI.create(link!!))
          hyperlink.toolTipText = link
          hyperlink.clickedColor = hyperlink.unclickedColor
          hyperlink.border = JBUI.Borders.emptyTop(5)
          hyperlink.isEnabled = true
          section.add(hyperlink)
        })
    add(section)
  }

  private fun addHelpSection(title: String, content: String?) {
    val section = JPanel()
    section.border = JBUI.Borders.empty(10)

    section.layout = BoxLayout(section, BoxLayout.Y_AXIS)

    val font = UIUtil.getLabelFont()
    val headingFontSize = font.size * 1.2f
    val headingFont = font.deriveFont(Font.BOLD, headingFontSize)
    if (!title.isEmpty()) {
      val heading = JLabel()
      heading.font = headingFont
      heading.text = String.format("<html>%s</html>", title)
      section.add(heading)
    }

    val descriptionLabel = JLabel()
    descriptionLabel.font = if (title.isEmpty()) headingFont else font
    descriptionLabel.text = String.format("<html>%s</html>", content)
    descriptionLabel.border = JBUI.Borders.emptyTop(5)
    section.add(descriptionLabel)
    add(section)
  }

  fun setHelp(finding: Any?, filename: String?) {
    this.vulnerability = null
    this.misconfig = null
    this.secret = null
    this.filepath = filename

    when (finding) {
      is Misconfiguration -> this.misconfig = finding
      is Vulnerability -> this.vulnerability = finding
      is Secret -> this.secret = finding
      is Result -> this.commercialResult = finding
    }

    updateHelp()
    this.validate()
    this.repaint()
  }

  private fun convertSeverity(severity: String): String {
    return when (severity) {
      "4", "CRITICAL" -> "Critical"
      "3", "HIGH" -> "High"
      "2", "MEDIUM" -> "Medium"
      "1", "LOW" -> "Low"
      else -> "Unknown"
    }
  }
}

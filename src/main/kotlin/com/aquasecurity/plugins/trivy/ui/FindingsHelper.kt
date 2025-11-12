package com.aquasecurity.plugins.trivy.ui

import com.aquasecurity.plugins.trivy.model.commercial.Result
import com.aquasecurity.plugins.trivy.model.report.Misconfiguration
import com.aquasecurity.plugins.trivy.model.report.Sast
import com.aquasecurity.plugins.trivy.model.report.Secret
import com.aquasecurity.plugins.trivy.model.report.Vulnerability
import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.Font
import java.awt.Rectangle
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.SwingUtilities

class FindingsHelper : ScrollablePanel() {
  private var finding: Any? = null
  private var filepath: String? = null
  private var vulnerability: Vulnerability? = null
  private var misconfig: Misconfiguration? = null
  private var secret: Secret? = null
  private var sast: Sast? = null
  private var commercialResult: Result? = null

  init {
    layout = BoxLayout(this, BoxLayout.Y_AXIS)
    border = JBUI.Borders.empty(2, 10)
    background = JBColor.PanelBackground

    // make the panel selectable
    isFocusable = true
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
      addHelpSection(vulnerability?.title!!, vulnerability?.description)
      addHelpSection("ID", vulnerability?.vulnerabilityId)
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

    if (this.sast != null) {
      val title = sast?.title.toString().lowercase().replaceFirstChar { it.uppercase() }
      val confidence =
          sast?.confidence?.toString()?.lowercase()?.replaceFirstChar { it.uppercase() }
      sast?.impact?.toString()?.lowercase()?.replaceFirstChar { it.uppercase() }
      val likelihood =
          sast?.likelihood?.toString()?.lowercase()?.replaceFirstChar { it.uppercase() }

      var owasp = ""
      sast?.owasp?.let { owasp = sast!!.owasp!!.joinToString("\n") }

      addHelpSection(title, "")
      addHelpSection(sast?.cwe ?: "N/A", sast?.message)
      addHelpSection("Severity", convertSeverity(sast?.severity.toString()))
      addHelpSection("Confidence", confidence)
      addHelpSection("Likelihood", likelihood)
      addHelpSection("OWASP", owasp)
      addHelpSection("Remediation", sast?.remediation?.toString() ?: "N/A")
      addHelpSection("Filename", filepath)

      if (sast?.references!!.isNotEmpty()) {
        addLinkSection(sast?.references!!)
      }
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
      if (fix == null || fix == "null" || fix.isEmpty()) {
        fix = "N/A"
      }
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
    val headingFontSize = font.size * 1.2f
    val headingFont = font.deriveFont(Font.BOLD, headingFontSize)
    section.add(createLabel("References", headingFont))

    links.forEach({ section.add(createLinkLabel(it, font)) })
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
      section.add(createLabel(title, headingFont))
    }
    if (content != null && !content.isEmpty()) {
      section.add(createLabel(content, font))
    }
    add(section)
  }

  private fun createLabel(content: String, font: Font): JTextArea {
    val label = JTextArea(content)
    label.font = font
    label.isEditable = false
    label.isOpaque = false
    label.background = JBColor.PanelBackground
    label.border = JBUI.Borders.emptyTop(5)

    // Enable text wrapping
    label.lineWrap = true
    label.wrapStyleWord = true

    return label
  }

  private fun createLinkLabel(content: String, font: Font): JTextArea {
    val label = JTextArea(content)
    label.font = font
    label.isEditable = false
    label.isOpaque = false
    label.background = JBColor.PanelBackground
    label.border = JBUI.Borders.emptyTop(5)
    label.foreground = JBColor.BLUE
    label.cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR)
    label.addMouseListener(
        object : java.awt.event.MouseAdapter() {
          override fun mouseClicked(e: java.awt.event.MouseEvent?) {
            if (e != null && e.button == java.awt.event.MouseEvent.BUTTON1) {
              try {
                java.awt.Desktop.getDesktop().browse(java.net.URI(content))
              } catch (ex: Exception) {
                ex.printStackTrace()
              }
            }
          }
        }
    )

    // Enable text wrapping
    label.lineWrap = true
    label.wrapStyleWord = true

    return label
  }

  fun setHelp(finding: Any?, filename: String?) {
    // if it's already the existing finding, do nothing
    if (this.finding == finding) {
      return
    }
    this.vulnerability = null
    this.misconfig = null
    this.secret = null
    this.sast = null
    this.filepath = filename

    when (finding) {
      is Misconfiguration -> this.misconfig = finding
      is Vulnerability -> this.vulnerability = finding
      is Secret -> this.secret = finding
      is Sast -> this.sast = finding
      is Result -> this.commercialResult = finding
    }

    this.finding = finding

    updateHelp()

    // synchronisation hack to ensure that the text gets wrapped appropriately
    SwingUtilities.invokeLater {
      // Force layout recalculation
      invalidate()
      revalidate()
      repaint()
      scrollRectToVisible(Rectangle(0, 0, width, height))

      // Add another invokeLater to ensure proper wrapping after first layout pass
      SwingUtilities.invokeLater {
        revalidate()
        repaint()
      }
    }
  }

  private fun convertSeverity(severity: String): String {
    return when (severity) {
      "4",
      "CRITICAL" -> "Critical"

      "3",
      "HIGH" -> "High"

      "2",
      "MEDIUM" -> "Medium"

      "1",
      "LOW" -> "Low"
      else -> "Unknown"
    }
  }
}

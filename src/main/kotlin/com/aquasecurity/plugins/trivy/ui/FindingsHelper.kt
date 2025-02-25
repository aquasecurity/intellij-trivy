package com.aquasecurity.plugins.trivy.ui

import com.aquasecurity.plugins.trivy.model.Misconfiguration
import com.aquasecurity.plugins.trivy.model.Secret
import com.aquasecurity.plugins.trivy.model.Vulnerability
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import org.jdesktop.swingx.JXHyperlink
import java.awt.Font
import java.awt.event.ActionEvent
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

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        border = JBUI.Borders.empty(10)
        background = JBColor.PanelBackground
    }


    private fun updateHelp() {
        removeAll()
        if (this.misconfig == null && this.vulnerability == null && this.secret == null) {
            return
        }

        if (this.misconfig != null) {
            addHelpSection(misconfig!!.title!!, misconfig!!.description)
            addHelpSection("ID", misconfig!!.id)
            addHelpSection("Severity", misconfig!!.severity)
            addHelpSection("Resolution", misconfig!!.resolution)
            addHelpSection("Filename", filepath)

            if (misconfig!!.references!!.size > 0) {
                addLinkSection(misconfig!!.references!!)
            }
        }

        if (this.vulnerability != null) {
            addHelpSection("", vulnerability!!.vulnerabilityID)
            addHelpSection(vulnerability!!.title!!, vulnerability!!.description)
            addHelpSection("Severity", vulnerability!!.severity)
            addHelpSection("Package Name", vulnerability!!.pkgName)
            addHelpSection("Installed Version", vulnerability!!.installedVersion)
            addHelpSection("Fixed Version", vulnerability!!.fixedVersion)
            addHelpSection("Filename", this.filepath)


            if (vulnerability!!.references!!.size > 0) {
                addLinkSection(vulnerability!!.references!!)
            }
        }

        if (this.secret != null) {
            addHelpSection("", secret!!.title)
            addHelpSection("Severity", secret!!.severity)
            addHelpSection("Match", secret!!.match)
            addHelpSection("Filename", filepath)
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

        links.forEach(Consumer { link: String? ->
            val hyperlink = JXHyperlink()
            hyperlink.text = String.format("<html>%s</html>", link)
            hyperlink.setURI(URI.create(link))
            hyperlink.toolTipText = link
            hyperlink.clickedColor = hyperlink.unclickedColor
            hyperlink.border = JBUI.Borders.emptyTop(5)
            hyperlink.isEnabled = true
            hyperlink.addActionListener { e: ActionEvent -> BrowserUtil.browse(URI.create(e.actionCommand)) }
            section.add(hyperlink)
        })
        add(section)
    }

    private fun addHelpSection(title: String, content: String?) {
        val section = JPanel()
        section.border = JBUI.Borders.empty(10)

        section.layout = BoxLayout(section, BoxLayout.Y_AXIS)

        val font = UIUtil.getLabelFont()
        val headingFontSize = font.size * 1.5f
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

    fun setMisconfiguration(misconfig: Misconfiguration?, filepath: String?) {
        this.vulnerability = null
        this.secret = null
        this.misconfig = misconfig
        this.filepath = filepath
        updateHelp()
        this.validate()
        this.repaint()
    }

    fun setSecret(secret: Secret?, filepath: String?) {
        this.vulnerability = null
        this.misconfig = null
        this.secret = secret
        this.filepath = filepath
        updateHelp()
        this.validate()
        this.repaint()
    }

    fun setVulnerability(vulnerability: Vulnerability?, filename: String?) {
        this.misconfig = null
        this.secret = null
        this.vulnerability = vulnerability
        this.filepath = filename
        updateHelp()
        this.validate()
        this.repaint()
    }
}
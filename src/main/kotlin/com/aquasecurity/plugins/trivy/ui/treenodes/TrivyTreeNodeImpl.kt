package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.icons.TrivyIcons
import com.aquasecurity.plugins.trivy.model.report.Misconfiguration
import com.aquasecurity.plugins.trivy.model.report.Sast
import com.aquasecurity.plugins.trivy.model.report.Secret
import com.aquasecurity.plugins.trivy.model.report.Vulnerability
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

class TrivyTreeNodeImpl(fileType: String, value: Any) : TrivyTreeNode, DefaultMutableTreeNode() {
  private val value: Any = value
  override val icon: Icon?
  override val title: String?
  override val tooltip: String?

  init {
    require(
        value is Vulnerability || value is Misconfiguration || value is Secret || value is Sast) {
          "Value must be of type Vulnerability, Misconfiguration, Secret, or Sast"
        }

    when (value) {
      is Vulnerability -> {
        icon = IconHelper.getFileIcon(fileType)
        title = value.pkgName
        tooltip = ""
      }

      is Misconfiguration -> {
        icon = IconHelper.getFileIcon(fileType)
        title = value.title
        tooltip = ""
      }

      is Secret -> {
        icon = TrivyIcons.Companion.Secret
        title = value.title
        tooltip = ""
      }

      is Sast -> {
        icon = IconHelper.getFileIcon(fileType)
        title = value.title
        tooltip = ""
      }

      else -> {
        icon = null
        title = null
        tooltip = null
      }
    }
  }
}

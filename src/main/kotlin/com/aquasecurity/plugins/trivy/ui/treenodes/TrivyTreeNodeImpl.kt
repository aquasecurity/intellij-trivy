package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.icons.TrivyIcons
import com.aquasecurity.plugins.trivy.model.oss.Misconfiguration
import com.aquasecurity.plugins.trivy.model.oss.Secret
import com.aquasecurity.plugins.trivy.model.oss.Vulnerability
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

class TrivyTreeNodeImpl(fileType: String, value: Any) : TrivyTreeNode, DefaultMutableTreeNode() {
  private val value: Any = value
  override val icon: Icon?
  override val title: String?
  override val tooltip: String?

  init {
    require(value is Vulnerability || value is Misconfiguration || value is Secret)

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

      else -> {
        icon = null
        title = null
        tooltip = null
      }
    }
  }
}

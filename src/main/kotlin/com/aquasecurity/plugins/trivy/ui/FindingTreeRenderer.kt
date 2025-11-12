package com.aquasecurity.plugins.trivy.ui

import com.aquasecurity.plugins.trivy.ui.treenodes.TrivyTreeNode
import java.awt.Color
import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer

internal class FindingTreeRenderer : DefaultTreeCellRenderer() {
  companion object {
    private val TransparentColor = Color(0, 0, 0, 0)
  }

  override fun getTreeCellRendererComponent(
      tree: JTree,
      value: Any,
      sel: Boolean,
      expanded: Boolean,
      leaf: Boolean,
      row: Int,
      hasFocus: Boolean,
  ): Component {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)

    if (value is TrivyTreeNode) {
      val node = value
      icon = node.icon
      text = node.title
      setBackgroundNonSelectionColor(TransparentColor)
      toolTipText = node.tooltip
    }
    return this
  }
}

package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.icons.TrivyIcons
import com.aquasecurity.plugins.trivy.model.Misconfiguration
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

internal class MisconfigurationTreeNode(val misconfiguration: Misconfiguration) :
    DefaultMutableTreeNode(misconfiguration), TrivyTreeNode {
    override val icon: Icon?
        get() = TrivyIcons.Trivy
    override val title: String?
        get() = misconfiguration.id!!
    override val tooltip: String?
        get() = misconfiguration.description!!
}
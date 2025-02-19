package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.model.Vulnerability
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

internal class VulnerablePackageTreeNode(val vulnerability: Vulnerability) :
    DefaultMutableTreeNode(vulnerability), TrivyTreeNode {
    override val icon: Icon?
        get() = null

    override val title: String
        get() = vulnerability.pkgName!!

    override val tooltip: String
        get() = vulnerability.description!!
}
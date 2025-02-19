package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.icons.TrivyIcons
import com.aquasecurity.plugins.trivy.model.Location
import com.aquasecurity.plugins.trivy.model.Secret
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

class SecretTreeNode(val secret: Secret, val filepath: String) :
    DefaultMutableTreeNode(secret), TrivyTreeNode {
    val location: Location
        get() = Location(filepath, secret.startLine, secret.endLine)
    override val icon: Icon?
        get() = TrivyIcons.Secret
    override val title: String?
        get() = secret.ruleID
    override val tooltip: String?
        get() = secret.title
}
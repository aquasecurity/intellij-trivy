package com.aquasecurity.plugins.trivy.ui.treenodes;

import com.aquasecurity.plugins.trivy.icons.TrivyIcons;
import com.aquasecurity.plugins.trivy.model.Misconfiguration;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

class MisconfigurationTreeNode extends DefaultMutableTreeNode implements TrivyTreeNode {

    public final Misconfiguration misconfiguration;

    public MisconfigurationTreeNode(Misconfiguration misconfiguration) {
        super(misconfiguration);
        this.misconfiguration = misconfiguration;
    }

    @Override
    public Icon getIcon() {
        return TrivyIcons.Trivy;
    }

    @Override
    public String getTitle() {
        return this.misconfiguration.id;
    }

    @Override
    public String getTooltip() {
        return this.misconfiguration.description;
    }
}

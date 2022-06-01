package com.aquasecurity.plugins.trivy.ui.treenodes;

import com.aquasecurity.plugins.trivy.icons.TrivyIcons;
import com.aquasecurity.plugins.trivy.model.Location;
import com.aquasecurity.plugins.trivy.model.Secret;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class SecretTreeNode extends DefaultMutableTreeNode implements TrivyTreeNode {

    public final Secret secret;
    public final String filepath;

    public SecretTreeNode(Secret secret, String filepath) {
        super(secret);
        this.secret = secret;
        this.filepath = filepath;
    }

    @Override
    public Icon getIcon() {
        return TrivyIcons.Secret;
    }

    @Override
    public String getTitle() {
        return this.secret.ruleID   ;
    }

    @Override
    public String getTooltip() {
        return this.secret.title;
    }

    public Secret getSecret() {
        return secret;
    }

    public Location getLocation() {

        return new Location(filepath, secret.startLine, secret.endLine);
    }
}

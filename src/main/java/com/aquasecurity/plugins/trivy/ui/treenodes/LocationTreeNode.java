package com.aquasecurity.plugins.trivy.ui.treenodes;


import com.aquasecurity.plugins.trivy.model.Location;
import com.aquasecurity.plugins.trivy.model.Misconfiguration;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class LocationTreeNode extends DefaultMutableTreeNode implements TrivyTreeNode {

    private final Misconfiguration misconfiguration;
    private final String filetype;
    private final String filepath;


    public LocationTreeNode(String filepath, String fileType, Misconfiguration misconfiguration) {
        this.misconfiguration = misconfiguration;
        this.filepath = filepath;
        this.filetype = fileType;
    }

    @Override
    public Icon getIcon() {
        return IconHelper.getFileIcon(this.filetype);
    }

    @Override
    public String getTitle() {
        if (this.misconfiguration.iacMetadata == null) {
            return filepath;
        }

        String lineDetails = String.format("%d", misconfiguration.iacMetadata.startLine);
        if (misconfiguration.iacMetadata.startLine != misconfiguration.iacMetadata.endLine) {
            lineDetails = String.format("%s-%d", lineDetails, misconfiguration.iacMetadata.endLine);
        }
        return String.format("%s:[%s]", this.filepath, lineDetails);
    }

    @Override
    public String getTooltip() {
        return "";
    }

    public Location getLocation() {
        int startline =1;
        int endline = 1;
        if (misconfiguration.causeMetadata != null) {
            startline = misconfiguration.causeMetadata.startLine;
            endline = misconfiguration.causeMetadata.endLine;
        } else if (misconfiguration.iacMetadata != null) {
            startline = misconfiguration.iacMetadata.startLine;
            endline = misconfiguration.iacMetadata.endLine;
        }

        if (startline <= 0) {
            startline = 1;
        }

        if (endline <= 0) {
            endline = 1;
        }

         return new Location(filepath, startline, endline);
    }

    public Misconfiguration getMisconfiguration() {
        return this.misconfiguration;
    }
}

package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.model.Location
import com.aquasecurity.plugins.trivy.model.Misconfiguration
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode


class LocationTreeNode(
    private val filepath: String,
    private val filetype: String,
    val misconfiguration: Misconfiguration
) :
    DefaultMutableTreeNode(), TrivyTreeNode {

    val location: Location
        get() {
            var startline = 1
            var endline = 1
            if (misconfiguration.causeMetadata != null) {
                startline = misconfiguration.causeMetadata!!.startLine
                endline = misconfiguration.causeMetadata!!.endLine
            } else if (misconfiguration.iacMetadata != null) {
                startline = misconfiguration.iacMetadata!!.startLine
                endline = misconfiguration.iacMetadata!!.endLine
            }

            if (startline <= 0) {
                startline = 1
            }

            if (endline <= 0) {
                endline = 1
            }

            return Location(filepath, startline, endline)
        }
    override val icon: Icon?
        get() = IconHelper.getFileIcon(this.filetype)
    override val title: String?
        get() {
            if (misconfiguration.iacMetadata == null) {
                return filepath
            }

            var lineDetails = String.format("%d", misconfiguration.iacMetadata!!.startLine)
            if (misconfiguration.iacMetadata!!.startLine != misconfiguration.iacMetadata!!.endLine) {
                lineDetails = String.format("%s-%d", lineDetails, misconfiguration.iacMetadata!!.endLine)
            }
            return String.format("%s:[%s]", this.filepath, lineDetails)
        }
    override val tooltip: String?
        get() = ""
}
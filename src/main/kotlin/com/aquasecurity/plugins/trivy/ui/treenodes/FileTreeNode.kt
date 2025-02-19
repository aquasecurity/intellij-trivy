package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.model.Misconfiguration
import com.aquasecurity.plugins.trivy.model.Result
import com.aquasecurity.plugins.trivy.model.Secret
import com.aquasecurity.plugins.trivy.model.Vulnerability
import java.util.function.Consumer
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

class FileTreeNode(result: Result?) : DefaultMutableTreeNode(), TrivyTreeNode {
    val target: String = result!!.target!!
    private val type: String? = result!!.type
    private val className: String? = result!!.ClassName

    init {
        update(result!!)
    }

    fun update(result: Result) {
        if (result.misconfigurations != null && result.misconfigurations!!.size > 0) {
            val visited: MutableList<MisconfigurationTreeNode> = ArrayList()
            result.misconfigurations!!.forEach(Consumer { ms: Misconfiguration ->
                if (visited.stream().noneMatch { v: MisconfigurationTreeNode -> v.misconfiguration.id == ms.id }) {
                    visited.add(MisconfigurationTreeNode(ms))
                }
                val findingNode =
                    visited.stream().filter { vf: MisconfigurationTreeNode -> vf.misconfiguration.id == ms.id }
                        .findFirst()
                if (findingNode.isPresent) {
                    val node = findingNode.get()
                    node.add(LocationTreeNode(result.target.toString(), result.type.toString(), ms))
                }
            })

            visited.forEach(Consumer { newChild: MisconfigurationTreeNode? -> this.add(newChild) })
        }

        if (result.vulnerabilities != null && result.vulnerabilities!!.isNotEmpty()) {
            val visited: MutableList<VulnerablePackageTreeNode> = ArrayList()
            result.vulnerabilities!!.forEach(Consumer { vulnerability: Vulnerability ->
                if (visited.stream()
                        .noneMatch { v: VulnerablePackageTreeNode -> v.vulnerability.pkgName == vulnerability.pkgName }
                ) {
                    visited.add(VulnerablePackageTreeNode(vulnerability))
                }
                val findingNode = visited.stream()
                    .filter { vf: VulnerablePackageTreeNode -> vf.vulnerability.pkgName == vulnerability.pkgName }
                    .findFirst()
                if (findingNode.isPresent) {
                    val node = findingNode.get()
                    node.add(VulnerabilityTreeNode(vulnerability, result.target.toString()))
                }
            })

            visited.forEach(Consumer { newChild: VulnerablePackageTreeNode? -> this.add(newChild) })
        }

        if (result.secrets != null && result.secrets!!.size > 0) {
            val visited: MutableList<SecretTreeNode> = ArrayList()
            result.secrets!!.forEach(Consumer { secret: Secret ->
                if (visited.stream().noneMatch { v: SecretTreeNode -> v.secret.ruleID == secret.ruleID }) {
                    visited.add(SecretTreeNode(secret, result.target.toString()))
                }
                val findingNode =
                    visited.stream().filter { vf: SecretTreeNode -> vf.secret.ruleID == secret.ruleID }.findFirst()
                if (findingNode.isPresent) {
                    findingNode.get()
                    //node.add(new SecretTreeNode(secret, result.target));
                }
            })

            visited.forEach(Consumer { newChild: SecretTreeNode? -> this.add(newChild) })
        }
    }


    override val icon: Icon?
        get() {
            var iconType = this.type
            if (iconType == null || iconType == "") {
                iconType = this.className
            }
            return IconHelper.getFileIcon(iconType.toString())
        }
    override val title: String?
        get() = this.target
    override val tooltip: String?
        get() = ""
}
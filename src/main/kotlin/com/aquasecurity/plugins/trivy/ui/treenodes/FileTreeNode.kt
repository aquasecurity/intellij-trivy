package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.model.oss.Misconfiguration
import com.aquasecurity.plugins.trivy.model.oss.Result
import com.aquasecurity.plugins.trivy.model.oss.Secret
import com.aquasecurity.plugins.trivy.model.oss.Vulnerability
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
    if (result.misconfigurations != null && result.misconfigurations!!.isNotEmpty()) {
      val visited: MutableList<LocationTreeNode> = ArrayList()
      // sort the misconfigurations
      result.misconfigurations = result.misconfigurations!!.sortedBy { it.severity }
      result.misconfigurations!!.forEach(
          Consumer { ms: Misconfiguration ->
            // get count of misconfigurations with the same title
            val count =
                result.misconfigurations!!
                    .stream()
                    .filter { v: Misconfiguration -> v.title == ms.title }
                    .count()
            if (visited.stream().noneMatch { v: LocationTreeNode -> v.title == ms.title }) {
              visited.add(LocationTreeNode(result.target.toString(), result.type.toString(), ms))
            }
            if (count > 1) {
              val findingNode =
                  visited.stream().filter { vf: TrivyTreeNode -> vf.title == ms.title }.findFirst()
              if (findingNode.isPresent) {
                val node = findingNode.get()
                node.add(
                    LocationTreeNode(result.target.toString(), result.type.toString(), ms, true))
              }
            }
          })
      visited.forEach(Consumer { newChild: LocationTreeNode? -> this.add(newChild) })
    }

    if (result.vulnerabilities != null && result.vulnerabilities!!.isNotEmpty()) {
      val visited: MutableList<TrivyTreeNodeImpl> = ArrayList()
      // sort the vulnerabilities
      result.vulnerabilities = result.vulnerabilities!!.sortedBy { it.severity }
      result.vulnerabilities!!.forEach(
          Consumer { vulnerability: Vulnerability ->
            if (visited.stream().noneMatch { v: TrivyTreeNodeImpl ->
              v.title == vulnerability.pkgName
            }) {
              visited.add(TrivyTreeNodeImpl(result.type.toString(), vulnerability))
            }
            val findingNode =
                visited
                    .stream()
                    .filter { vf: TrivyTreeNodeImpl -> vf.title == vulnerability.pkgName }
                    .findFirst()
            if (findingNode.isPresent) {
              val node = findingNode.get()
              node.add(
                  LocationTreeNode(result.target.toString(), result.type.toString(), vulnerability))
            }
          })
      visited.forEach(Consumer { newChild: TrivyTreeNodeImpl? -> this.add(newChild) })
    }

    if (result.secrets != null && result.secrets!!.isNotEmpty()) {
      val visited: MutableList<LocationTreeNode> = ArrayList()
      // sort the secrets
        result.secrets = result.secrets?.sortedBy { it.title }
        result.secrets?.forEach(
          Consumer { secret: Secret ->
            if (visited.stream().noneMatch { v: LocationTreeNode -> v.title == secret.title }) {
              visited.add(
                  LocationTreeNode(result.target.toString(), result.type.toString(), secret))
            }
          })
      visited.forEach(Consumer { newChild: LocationTreeNode? -> this.add(newChild) })
    }
  }

  override val icon: Icon?
    get() {
      var iconType = this.type
      if (iconType == null || iconType == "") {
        // get the extension from the file
        val extension = this.target.substring(this.target.lastIndexOf(".") + 1)
        if (extension != "") {
          iconType = extension
        } else {
          iconType = this.className
        }
      }
      return IconHelper.getFileIcon(iconType.toString())
    }

  override val title: String?
    get() = this.target

  override val tooltip: String?
    get() = ""
}

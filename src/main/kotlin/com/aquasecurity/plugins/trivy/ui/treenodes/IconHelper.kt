package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.icons.TrivyIcons
import javax.swing.Icon

object IconHelper {
  internal fun getFileIcon(type: String): Icon {
    return when (type) {
      "cargo" -> TrivyIcons.Cargo
      "cloudformation" -> TrivyIcons.CloudFormation
      "dockerfile" -> TrivyIcons.Docker
      "gemspec" -> TrivyIcons.Gem
      "gobinary",
      "gomod" -> TrivyIcons.Go
      "html" -> TrivyIcons.Html
      "jar",
      "pom" -> TrivyIcons.Java
      "javascript" -> TrivyIcons.Javascript
      "js" -> TrivyIcons.Javascript
      "node" -> TrivyIcons.Node
      "npm" -> TrivyIcons.Npm
      "nuget" -> TrivyIcons.Nuget
      "pip",
      "pipenv",
      "python-pkg" -> TrivyIcons.Python
      "secret" -> TrivyIcons.Secret
      "terraform" -> TrivyIcons.Terraform
      "yarn" -> TrivyIcons.Yarn
      "yaml" -> TrivyIcons.Yaml
      else -> TrivyIcons.Text
    }
  }
}

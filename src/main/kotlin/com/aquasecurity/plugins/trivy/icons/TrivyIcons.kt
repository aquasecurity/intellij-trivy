package com.aquasecurity.plugins.trivy.icons

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

interface TrivyIcons {
  companion object {
    val Trivy: Icon = IconLoader.getIcon("/icons/trivy.png", TrivyIcons::class.java)
    val Critical: Icon = IconLoader.getIcon("/icons/critical.svg", TrivyIcons::class.java)
    val High: Icon = IconLoader.getIcon("/icons/high.svg", TrivyIcons::class.java)
    val Medium: Icon = IconLoader.getIcon("/icons/medium.svg", TrivyIcons::class.java)
    val Low: Icon = IconLoader.getIcon("/icons/low.svg", TrivyIcons::class.java)

    val Cargo: Icon = IconLoader.getIcon("/icons/cargo.svg", TrivyIcons::class.java)
    val CloudFormation: Icon =
        IconLoader.getIcon("/icons/cloudformation.svg", TrivyIcons::class.java)
    val Docker: Icon = IconLoader.getIcon("/icons/dockerfile.svg", TrivyIcons::class.java)
    val Gem: Icon = IconLoader.getIcon("/icons/ruby.svg", TrivyIcons::class.java)
    val Go: Icon = IconLoader.getIcon("/icons/go.svg", TrivyIcons::class.java)
    val Java: Icon = IconLoader.getIcon("/icons/java.svg", TrivyIcons::class.java)
    val Javascript: Icon = IconLoader.getIcon("/icons/javascript.svg", TrivyIcons::class.java)
    val Node: Icon = IconLoader.getIcon("/icons/node.svg", TrivyIcons::class.java)
    val Npm: Icon = IconLoader.getIcon("/icons/npm.svg", TrivyIcons::class.java)
    val Nuget: Icon = IconLoader.getIcon("/icons/nuget.svg", TrivyIcons::class.java)
    val Python: Icon = IconLoader.getIcon("/icons/python.svg", TrivyIcons::class.java)
    val Text: Icon = IconLoader.getIcon("/icons/text.svg", TrivyIcons::class.java)

    val Secret: Icon = IconLoader.getIcon("/icons/secret.svg", TrivyIcons::class.java)
    val Terraform: Icon = IconLoader.getIcon("/icons/terraform.svg", TrivyIcons::class.java)
    val Yaml: Icon = IconLoader.getIcon("/icons/yaml.svg", TrivyIcons::class.java)
    val Yarn: Icon = IconLoader.getIcon("/icons/yarn.svg", TrivyIcons::class.java)
    val Policy: Icon = IconLoader.getIcon("/icons/policy.svg", TrivyIcons::class.java)

    val Unidentified: Icon = IconLoader.getIcon("/icons/text.svg", TrivyIcons::class.java)
  }
}

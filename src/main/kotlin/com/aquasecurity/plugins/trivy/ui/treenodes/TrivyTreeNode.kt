package com.aquasecurity.plugins.trivy.ui.treenodes

import javax.swing.Icon

interface TrivyTreeNode {
  val icon: Icon?

  val title: String?

  val tooltip: String?
}

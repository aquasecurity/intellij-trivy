package com.aquasecurity.plugins.trivy.ui.treenodes;

import javax.swing.*;

public interface TrivyTreeNode {
    Icon getIcon();

    String getTitle();

    String getTooltip();
}

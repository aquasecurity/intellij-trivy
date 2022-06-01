package com.aquasecurity.plugins.trivy.ui.treenodes;

import com.aquasecurity.plugins.trivy.model.Result;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileTreeNode extends DefaultMutableTreeNode implements TrivyTreeNode {

    private String target;
    private String type;
    private String className;

    public FileTreeNode(Result result) {
        if (result == null) {
            return;
        }
        this.target = result.target;
        this.type = result.type;
        this.className = result.ClassName;

        update(result);
    }

public void update(Result result) {

        if (result.misconfigurations != null && result.misconfigurations.size() > 0) {
            List<MisconfigurationTreeNode> visited = new ArrayList<>();
            result.misconfigurations.forEach(ms -> {

                if (visited.stream().noneMatch(v -> Objects.equals(v.misconfiguration.id, ms.id))) {
                    visited.add(new MisconfigurationTreeNode(ms));
                }
                Optional<MisconfigurationTreeNode> findingNode = visited.stream().filter(vf -> Objects.equals(vf.misconfiguration.id, ms.id)).findFirst();
                if (findingNode.isPresent()) {
                    MisconfigurationTreeNode node = findingNode.get();
                    node.add(new LocationTreeNode(result.target, result.type, ms));
                }
            });

            visited.forEach(this::add);
        }

        if (result.vulnerabilities != null && result.vulnerabilities.size() > 0) {
            List<VulnerablePackageTreeNode> visited = new ArrayList<>();
            result.vulnerabilities.forEach(vulnerability -> {

                if (visited.stream().noneMatch(v -> Objects.equals(v.vulnerability.pkgName, vulnerability.pkgName))) {
                    visited.add(new VulnerablePackageTreeNode(vulnerability));
                }
                Optional<VulnerablePackageTreeNode> findingNode = visited.stream().filter(vf -> Objects.equals(vf.vulnerability.pkgName, vulnerability.pkgName)).findFirst();
                if (findingNode.isPresent()) {
                    VulnerablePackageTreeNode node = findingNode.get();
                    node.add(new VulnerabilityTreeNode(vulnerability, result.target));
                }
            });

            visited.forEach(this::add);
        }

        if (result.secrets != null && result.secrets.size() > 0) {
            List<SecretTreeNode> visited = new ArrayList<>();
            result.secrets.forEach(secret -> {
                if (visited.stream().noneMatch(v -> Objects.equals(v.secret.ruleID, secret.ruleID))) {
                    visited.add(new SecretTreeNode(secret, result.target));
                }
                Optional<SecretTreeNode> findingNode = visited.stream().filter(vf -> Objects.equals(vf.secret.ruleID, secret.ruleID)).findFirst();
                if (findingNode.isPresent()) {
                    SecretTreeNode node = findingNode.get();
                    //node.add(new SecretTreeNode(secret, result.target));
                }
            });

            visited.forEach(this::add);
        }


    }

    @Override
    public Icon getIcon() {
        String iconType = this.type;
        if (iconType == null || Objects.equals(iconType, "")) {
            iconType = this.className;
        }
        return IconHelper.getFileIcon(iconType);
    }

    @Override
    public String getTitle() {
        return this.target;
    }

    @Override
    public String getTooltip() {
        return "";
    }

    public String getTarget() {
        return this.target;
    }

}


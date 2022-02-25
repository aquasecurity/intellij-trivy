package com.aquasecurity.plugins.trivy.ui.treenodes;

import com.aquasecurity.plugins.trivy.model.Result;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileTreeNode extends DefaultMutableTreeNode implements TrivyTreeNode {

    private final Result result;

    public FileTreeNode(Result result) {
        this.result = result;

        if (result == null) {
            return;
        }

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
                    visited.add(new VulnerablePackageTreeNode(vulnerability ));
                }
                Optional<VulnerablePackageTreeNode> findingNode = visited.stream().filter(vf -> Objects.equals(vf.vulnerability.pkgName, vulnerability.pkgName)).findFirst();
                if (findingNode.isPresent()) {
                    VulnerablePackageTreeNode node = findingNode.get();
                    node.add(new VulnerabilityTreeNode(vulnerability, result.target));
                }
            });

            visited.forEach(this::add);
        }


    }

    @Override
    public Icon getIcon() {
        return IconHelper.getFileIcon(result.type);
    }

    @Override
    public String getTitle() {
        return result.target;
    }

    @Override
    public String getTooltip() {
        return "";
    }


}


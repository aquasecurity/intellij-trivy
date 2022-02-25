package com.aquasecurity.plugins.trivy.ui.treenodes;

import com.aquasecurity.plugins.trivy.icons.TrivyIcons;
import com.aquasecurity.plugins.trivy.model.Result;

import javax.swing.*;

public class IconHelper {

    protected static Icon getFileIcon(String type) {
        switch (type) {
            case "cargo":
                return TrivyIcons.Cargo;
            case "cloudformation":
                return TrivyIcons.CloudFormation;
            case "dockerfile":
                return TrivyIcons.Docker;
            case "gemspec":
                return TrivyIcons.Gem;
            case "gobinary":
            case "gomod":
                return TrivyIcons.Go;
            case "jar":
            case "pom":
                return TrivyIcons.Java;
            case "javascript":
                return TrivyIcons.Javascript;
            case "node":
                return TrivyIcons.Node;
            case "npm":
                return TrivyIcons.Npm;
            case "nuget":
                return TrivyIcons.Nuget;
            case "pip":
            case "pipenv":
            case"python-pkg":
                return TrivyIcons.Python;
            case "terraform":
                return TrivyIcons.Terraform;
            case "yarn":
                return TrivyIcons.Yarn;
            case "yaml":
                return TrivyIcons.Yaml;
            default:
                return TrivyIcons.Trivy;
        }
    }
}

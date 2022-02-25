package com.aquasecurity.plugins.trivy.icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;


public interface TrivyIcons {
    Icon Trivy = IconLoader.getIcon("/icons/trivy.png", TrivyIcons.class);
    Icon Critical = IconLoader.getIcon("/icons/critical.svg", TrivyIcons.class);
    Icon High = IconLoader.getIcon("/icons/high.svg", TrivyIcons.class);
    Icon Medium = IconLoader.getIcon("/icons/medium.svg", TrivyIcons.class);
    Icon Low = IconLoader.getIcon("/icons/low.svg", TrivyIcons.class);

    Icon Cargo = IconLoader.getIcon("/icons/cargo.svg", TrivyIcons.class);
    Icon CloudFormation = IconLoader.getIcon("/icons/cloudformation.svg", TrivyIcons.class);
    Icon Docker = IconLoader.getIcon("/icons/dockerfile.svg", TrivyIcons.class);
    Icon Gem = IconLoader.getIcon("/icons/ruby.svg", TrivyIcons.class);
    Icon Go = IconLoader.getIcon("/icons/go.svg", TrivyIcons.class);
    Icon Java = IconLoader.getIcon("/icons/java.svg", TrivyIcons.class);
    Icon Javascript = IconLoader.getIcon("/icons/javascript.svg", TrivyIcons.class);
    Icon Node = IconLoader.getIcon("/icons/node.svg", TrivyIcons.class);
    Icon Npm = IconLoader.getIcon("/icons/npm.svg", TrivyIcons.class);
    Icon Nuget = IconLoader.getIcon("/icons/nuget.svg", TrivyIcons.class);
    Icon Python = IconLoader.getIcon("/icons/python.svg", TrivyIcons.class);
    Icon Terraform = IconLoader.getIcon("/icons/terraform.svg", TrivyIcons.class);
    Icon Yaml = IconLoader.getIcon("/icons/yaml.svg", TrivyIcons.class);
    Icon Yarn = IconLoader.getIcon("/icons/yarn.svg", TrivyIcons.class);
    Icon Unidentified = IconLoader.getIcon("/icons/trivy.svg", TrivyIcons.class);
}

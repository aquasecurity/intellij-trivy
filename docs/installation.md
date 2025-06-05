# Installation

This guide will help you install and set up the Trivy JetBrains plugin in your IntelliJ-based IDE.

## Prerequisites

- A supported JetBrains IDE (IDEA, GoLand, WebStorm, etc.)
- Trivy must be installed on your system. The plugin requires an external Trivy installation.

## Installing Trivy

Before using the plugin, you need to install Trivy on your system. For installation instructions, visit the [Trivy installation page](https://trivy.dev/latest/getting-started/installation/).

## Installing the Plugin

### From the JetBrains Marketplace

1. Open your JetBrains IDE
2. Navigate to Settings/Preferences → Plugins → Marketplace
3. Search for "Trivy Findings Explorer"
4. Click "Install" and restart your IDE when prompted

### Manual Installation

1. Download the plugin from the [JetBrains Plugin Repository](https://plugins.jetbrains.com/plugin/18690-trivy-findings-explorer)
2. Navigate to Settings/Preferences → Plugins
3. Click the gear icon and select "Install Plugin from Disk..."
4. Select the downloaded plugin file and restart your IDE when prompted

## Initial Configuration

After installation, you need to configure the plugin:

1. Navigate to Settings/Preferences → Tools → Trivy: Settings
2. Set the path to the Trivy binary if it's not in your system PATH
3. Choose which scanners to enable (vulnerabilities, misconfigurations, secrets)
4. Configure severity levels and other options according to your needs
5. Click "Apply" to save your settings

Now you're ready to use the Trivy plugin to scan your projects!

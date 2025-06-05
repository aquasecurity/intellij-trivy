# Quickstart

This guide will help you quickly get started with the Trivy JetBrains plugin.

## Prerequisites

Ensure you have:
- Installed the Trivy JetBrains plugin ([see installation guide](installation.md))
- Installed Trivy on your system and configured the path in the plugin settings

## Running Your First Scan

1. Open your project in the IDE
2. Open the Trivy Explorer tool window
   - Navigate to View → Tool Windows → Trivy Explorer
   - Alternatively, click on the Trivy icon in the tool window bar

3. Click the "Run Scan" button (the play icon) in the Trivy Explorer toolbar to start a scan

4. Wait for the scan to complete
   - A progress indicator will show while the scan is running
   - Results will be displayed in the Trivy Explorer window when finished

## Exploring the Results

The scan results are organized into categories:

- **Vulnerabilities**: Issues found in your project dependencies
- **Misconfigurations**: Issues in infrastructure as code (IaC) files such as Docker, Terraform, Kubernetes, etc.
- **Secrets**: Potential exposed secrets in your codebase

For each finding:

1. Click on an issue to view details in the bottom panel
2. Double-click on a finding to navigate to the affected file location
3. Review the details including severity, description, and remediation advice when available

## Next Steps

- Explore the [Features](features.md) documentation to learn more about what the plugin can do
- Configure the plugin through the [Settings](settings.md) to match your security requirements
- Check the [Troubleshooting](troubleshooting.md) guide if you encounter any issues

# Features

The Trivy JetBrains plugin offers a comprehensive set of features for vulnerability scanning and security analysis directly within your IDE.

## Core Scanning Capabilities

The plugin supports the following scan types:

### Vulnerability Scanning

- Detects vulnerabilities in package dependencies across various language ecosystems
- Shows vulnerability details including severity, description, and remediation advice
- Links to relevant CVE information
- Displays which packages are affected and their versions
- Option to filter by vulnerability severity (Critical, High, Medium, Low, Unknown)

### Misconfiguration Scanning

- Identifies security issues in infrastructure as code (IaC) files
- Supports various IaC formats including:
  - Dockerfile
  - Terraform
  - Kubernetes manifests
  - CloudFormation templates
  - YAML configurations
- Provides best practice recommendations for securing your infrastructure

### Secret Scanning

- Detects potentially exposed secrets in your codebase
- Identifies secrets such as API keys, passwords, and tokens
- Helps prevent accidental committing of sensitive information

## Interface Features

### Trivy Explorer Window

- Visual explorer interface to browse and filter findings
- Tree view organizing findings by file and severity
- Filter results by severity level
- Quick navigation to affected files and line numbers

### Finding Details Panel

- Detailed information about each finding
- Description of the vulnerability/misconfiguration/secret
- Severity indicator
- Remediation advice when available
- Links to additional resources

## Configuration Options

### Severity Filtering

- Configure which severity levels to include in scan results (Critical, High, Medium, Low, Unknown)
- Option to focus on only the most critical issues

### Scan Options

- Offline scanning capability for environments with limited connectivity
- Option to only show vulnerabilities with available fixes
- Configuration file support for custom scan settings
- Ignore file support to exclude specific findings

## Integration with Aqua Platform

For users with Aqua Platform access:

- Connect to the Aqua Platform using API credentials
- Region selection (US, EU, Singapore, Sydney)
- Visualize assurance policies in the Trivy Explorer
- Separate tab for Assurance Policies view

## Additional Features

- Direct access to settings from the Trivy Explorer toolbar
- Clear results option to reset the view
- Support for various file types and programming languages

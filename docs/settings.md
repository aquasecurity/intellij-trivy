# Settings

The Trivy JetBrains plugin offers various configuration options to customize its behavior according to your needs.

## Accessing Settings

1. Open your IDE settings/preferences dialog
2. Navigate to Tools → Trivy: Settings

Alternatively, click the settings icon in the Trivy Explorer toolbar.

## Basic Configuration

### Trivy Binary Path

- **Trivy binary**: Specify the path to the Trivy executable on your system
- If Trivy is in your system PATH, you can simply enter `trivy`
- Otherwise, provide the full path to the executable

## Scanner Options

Enable or disable specific scanning capabilities:

- **Enable vulnerability scanning**: Scan for vulnerabilities in dependencies
- **Enable misconfiguration scanning**: Scan for misconfigurations in IaC files
- **Enable secret scanning**: Scan for exposed secrets in your code

## Severity Levels

Choose which severity levels to include in scan results:

- Critical
- High
- Medium
- Low
- Unknown

## Additional Options

- **Offline scan**: Run scans without internet connectivity (uses local database)
- **Only show issues with fixes**: Filter to show only vulnerabilities that have available fixes

## Configuration Files

- **Use config file**: Enable using a Trivy configuration file
- **Config file path**: Specify the path to your Trivy configuration file
- **Use ignore file**: Enable using a Trivy ignore file
- **Ignore file path**: Specify the path to your Trivy ignore file

## Aqua Platform Integration

For users with Aqua Platform access:

- **Use Aqua Platform**: Enable integration with Aqua Platform
- **API Key**: Your Aqua Platform API key
- **API Secret**: Your Aqua Platform API secret
- **Region**: Select your Aqua Platform region (US, EU, Singapore, Sydney)
  - The Custom region is for self-hosted Aqua Platform instances - if you're using this, you will be provided with the required URLs

## Project-Level vs. Application-Level Settings

- Most settings are applied at the application level and affect all projects
- Some settings like config file path, ignore file path, and Aqua Platform usage are project-specific

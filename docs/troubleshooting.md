# Troubleshooting

This guide will help you resolve common issues encountered when using the Trivy JetBrains plugin.

## Trivy Not Found

**Symptoms:**
- Error message indicating Trivy executable not found
- Scans fail to start

**Solutions:**
1. Verify that Trivy is installed on your system
2. Check the Trivy path in Settings → Tools → Trivy: Settings
3. Make sure the path points to the correct Trivy executable
4. If using just `trivy` as the path, ensure Trivy is in your system PATH

## Scan Results Not Showing

**Symptoms:**
- Scan completes but no results are displayed
- "No vulnerabilities found" message when you expected findings

**Solutions:**
1. Check if severity filters in settings are too restrictive
2. Verify that the appropriate scanners are enabled (vulnerability, misconfiguration, secret)
3. Make sure the "Only show issues with fixes" option isn't filtering out your findings
4. Try running Trivy directly from the command line to verify it finds issues

## Cannot Open Files from Results

**Symptoms:**
- Clicking on findings doesn't open the corresponding file
- Error message when trying to navigate to a file

**Solutions:**
1. Make sure the file paths are correct and accessible
2. Check if the file is part of the current project
3. Verify that the file hasn't been moved or renamed since the scan
4. Try rerunning the scan

## Slow Scan Performance

**Symptoms:**
- Scans take an unusually long time to complete

**Solutions:**
1. Consider using the offline scan option if you're primarily concerned with known vulnerabilities
2. Limit the scan to only necessary severity levels
3. For large projects, consider scanning specific directories or files rather than the entire project
4. Ensure your Trivy database is up to date (`trivy --download-db-only`)

## Issues with Aqua Platform Integration

**Symptoms:**
- Cannot connect to Aqua Platform
- Assurance policy results not showing

**Solutions:**
1. Verify your API Key and API Secret are entered correctly
2. Ensure you have selected the correct region
3. Check your network connectivity to the Aqua Platform
4. Verify your Aqua Platform subscription is active

## General Troubleshooting Steps

1. **Check IDE logs**: Look for error messages in the IDE's log files (Help → Show Log in Explorer/Finder)
2. **Update the plugin**: Make sure you're using the latest version of the Trivy JetBrains plugin
3. **Update Trivy**: Ensure you're using a recent version of Trivy
4. **Restart IDE**: Sometimes a simple restart can resolve unexpected issues

If you continue to experience problems not addressed in this guide, consider:
- Checking for known issues on the [plugin's GitHub repository](https://github.com/aquasecurity/intellij-trivy/issues)
- Filing a new issue if you've discovered a bug

package com.aquasecurity.plugins.trivy.actions.tasks

import com.aquasecurity.plugins.trivy.binary.TrivyBinary
import com.aquasecurity.plugins.trivy.binary.TrivyBinary.Companion.downloadBinary
import com.aquasecurity.plugins.trivy.settings.TrivySettingState
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import java.io.File
import java.nio.file.Paths
import java.util.*

internal class TrivyDownloadBinaryTask(
    private val project: Project,
    val initial: Boolean,
    val callback: (() -> Unit)? = null
) :
    Backgroundable(project, "Downloading Trivy", false), Runnable {

    override fun run(indicator: ProgressIndicator) {
        this.run()
    }

    override fun run() {
        if (!initial) {
            // this is a force download
            getTrivy()
        } else {
            if (TrivySettingState.instance.trivyPath.isEmpty()) {
                getTrivy()
            } else if (File(TrivySettingState.instance.trivyPath).exists()) {
                TrivySettingState.instance.binaryPath = TrivySettingState.instance.trivyPath
            } else {
                TrivyBinary.binaryFile = findFileInPath(TrivySettingState.instance.trivyPath)
                if (TrivyBinary.binaryFile.isEmpty()) {
                    getTrivy()
                }
            }
        }
        if (callback != null) {
            callback.invoke()
        }
    }

    private fun findFileInPath(fileName: String): String {
        // Get the PATH environment variable
        val pathEnv = System.getenv("PATH") ?: return ""
        // Split the PATH into individual directories
        val paths = pathEnv.split(File.pathSeparator)
        // Iterate over each directory
        for (path in paths) {
            val file = File(path, fileName)
            // Check if the file exists in this directory
            if (file.exists() && file.isFile) {
                return file.absolutePath
            }
        }
        return ""
    }

    private fun getTrivy() {
        var osName = System.getProperty("os.name").lowercase(Locale.getDefault())
        var arch = System.getProperty("os.arch").lowercase(Locale.getDefault())
        var suffix = "tar.gz"

        var binaryTarget = "trivy"

        if (arch == "aarch64") {
            arch = "arm64"
        } else if (arch == "x86_64" || arch == "amd64" || arch == "x64") {
            arch = "amd64"
        } else if (arch == "i386" || arch == "i686") {
            arch = "386"
        }

        if (osName.contains("win")) {
            osName = "windows"
            suffix = "zip"
            binaryTarget = "trivy.exe"
        } else if (osName.contains("mac")) {
            osName = "macos"
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            osName = "linux"
        }
        PluginManagerCore.getPlugin(PluginId.getId("com.aquasecurity.plugins.intellij-Trivy"))
            ?.pluginPath
            ?.let {
                val targetFile = Paths.get(it.toAbsolutePath().toString(), binaryTarget).toFile()
                if (downloadBinary(project, osName, arch, suffix, targetFile, initial)) {
                    TrivySettingState.instance.trivyPath = targetFile.absolutePath
                    TrivyBinary.binaryFile = targetFile.absolutePath
                }
        }
    }
}

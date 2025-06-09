package com.aquasecurity.plugins.trivy.binary

import com.aquasecurity.plugins.trivy.actions.CheckForTrivyAction
import com.aquasecurity.plugins.trivy.settings.TrivySettingState
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.intellij.openapi.project.Project
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.util.zip.GZIPInputStream
import kotlin.io.path.createTempDirectory

const val releaseUrl = "https://get.trivy.dev/trivy"
const val checksumUrl = "https://github.com/aquasecurity/trivy/releases/download"

class TrivyBinary {
    companion object {
        @JvmStatic
        var binaryFile: String = ""

        fun downloadBinary(
            project: Project,
            os: String,
            arch: String,
            suffix: String,
            target: File,
            initial: Boolean
        ): Boolean {
            if (target.exists() && initial) {
                println("Binary already exists at ${target.absolutePath}, setting as binary file")
                TrivySettingState.instance.trivyPath = target.absolutePath
                return false
            }

            val latestTag = getLatestGithubTag()

            var targetSuffix = suffix
            if (os == "windows") {
                targetSuffix = ".zip"
            }

            val tmpFile = kotlin.io.path.createTempFile("trivy-${os}-${arch}", targetSuffix ).toFile()
            downloadFile("${releaseUrl}?os=${os}&arch=${arch}&type=${suffix}", tmpFile)
            val checksums = fetchUrl("${checksumUrl}/v${latestTag}/trivy_${latestTag}_checksums.txt").split("\n")
                .filter { it.isNotBlank() }
                .map { it.trim() }
                .map { it.split("\\s+".toRegex()) }
                .filter { it.size == 2 }
                .map { Pair(it[0], it[1]) }

            val calculatedChecksum = calculateChecksum(tmpFile)
            for (checksum in checksums) {
                if (checksum.second == tmpFile.name) {
                    val expectedChecksum = checksum.first
                    if (expectedChecksum.isNotEmpty() && expectedChecksum == calculatedChecksum) {
                        break
                    } else {
                        TrivyNotificationGroup.notifyError(
                            project,
                            "Checksum for downloaded file is empty, not using download"
                        )
                        return false
                    }
                }
            }

            if (os == "windows") {
                unZip(tmpFile, target)
            } else {
                unTar(tmpFile, target)
            }
            target.setExecutable(true)
            CheckForTrivyAction.run(project)
            TrivyNotificationGroup.notifyInformation(project, "Downloaded Trivy")
            return true
        }

        private fun fetchUrl(url: String): String {
            val connection = URL(url).openConnection() as HttpURLConnection
            // Add proper user agent to avoid GitHub API limitations
            connection.setRequestProperty("User-Agent", "IntelliJ-Trivy-Plugin")

            // Set reasonable timeouts
            connection.connectTimeout = 10000 // 10 seconds
            connection.readTimeout = 30000 // 30 seconds

            try {
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    throw Exception("Failed to fetch URL $url, response code: $responseCode")
                }
            } catch (e: Exception) {
                // Log the error and re-throw with more context
                val errorMessage = "Failed to fetch URL $url: ${e.message}"
                println(errorMessage) // Replace with proper logging
                throw Exception(errorMessage, e)
            }
        }

        private fun extractFilenameFromContentDisposition(contentDisposition: String?): String? {
            return contentDisposition?.let {
                val regex = Regex("filename=([^\"]+)")
                val matchResult = regex.find(it)
                matchResult?.groups?.get(1)?.value
            }
        }

        private fun downloadFile(url: String, outputFile: File) {
            val connection = URL(url).openConnection() as HttpURLConnection

            // Get the Content-Disposition header
            val contentDisposition = connection.getHeaderField("Content-Disposition")
            val filename = extractFilenameFromContentDisposition(contentDisposition)

            // Create a new output file with the name from Content-Disposition if different
            val finalOutputFile = if (filename != outputFile.name) {
                File(outputFile.parentFile, filename)
            } else {
                outputFile
            }

            connection.inputStream.use { input ->
                FileOutputStream(finalOutputFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Rename the output file to match the requested name if different
            if (finalOutputFile != outputFile && finalOutputFile.exists()) {
                finalOutputFile.renameTo(outputFile)
            }
        }

        private fun calculateChecksum(file: File): String {
            val digest = MessageDigest.getInstance("SHA-256")
            file.inputStream().use { input ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
            }
            return digest.digest().joinToString("") { "%02x".format(it) }
        }

        private fun unTar(tarFile: File, targetFile: File) {
            val outputDir = createTempDirectory().toFile()
            val tarInput: InputStream = if (tarFile.extension == "gz") {
                GZIPInputStream(FileInputStream(tarFile))
            } else {
                FileInputStream(tarFile)
            }
            val tarArchiveInputStream = TarArchiveInputStream(tarInput)

            var entry: TarArchiveEntry? = tarArchiveInputStream.nextEntry
            while (entry != null) {
                val outputFile = File(outputDir, entry.name)
                if (entry.isDirectory) {
                    outputFile.mkdirs()
                } else {
                    outputFile.parentFile?.mkdirs()
                    FileOutputStream(outputFile).use { output ->
                        tarArchiveInputStream.copyTo(output)
                    }
                }
                val newFile = File(targetFile.parent, outputFile.relativeTo(outputDir).path)
                newFile.mkdirs()
                Files.move(outputFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                entry = tarArchiveInputStream.nextEntry
            }
            return
        }

        private fun unZip(zipFile: File, targetFile: File) {
            val outputDir = createTempDirectory().toFile()
            java.util.zip.ZipInputStream(FileInputStream(zipFile)).use { zipInput ->
                var entry = zipInput.nextEntry
                while (entry != null) {
                    val outputFile = File(outputDir, entry.name)
                    if (entry.isDirectory) {
                        outputFile.mkdirs()
                    } else {
                        outputFile.parentFile?.mkdirs()
                        FileOutputStream(outputFile).use { output ->
                            zipInput.copyTo(output)
                        }
                    }
                    val newFile = File(targetFile.parent, outputFile.relativeTo(outputDir).path)
                    newFile.mkdirs()
                    Files.move(outputFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    entry = zipInput.nextEntry
                }
            }
        }
    }
}
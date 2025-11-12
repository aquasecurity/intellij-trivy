package com.aquasecurity.plugins.trivy.settings

import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.intellij.openapi.project.Project
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest

class CredentialCheck {

  companion object {

    fun isValidCredentials(
        project: Project,
        apiKey: String,
        apiSecret: String,
        aquaUrl: String,
        aquaAuthUrl: String,
    ): Boolean {
      if (apiKey.isBlank() || apiSecret.isBlank() || aquaUrl.isBlank() || aquaAuthUrl.isBlank()) {
        TrivyNotificationGroup.notifyError(
            project,
            "API Key, API Secret, Aqua URL, or Aqua Auth URL is blank.",
        )
        return false
      }

      val body = """{"validity":120,"allowed_endpoints":["ANY:V2/*"]}"""
      val requestUrl = "$aquaAuthUrl/v2/tokens"
      // Use milliseconds since epoch, as in the TypeScript code
      val timestamp = System.currentTimeMillis().toString()
      val someString = timestamp + "POST" + "/v2/tokens" + body
      var hexString: String = ""

      javax.crypto.Mac.getInstance("HmacSHA256").apply {
        init(javax.crypto.spec.SecretKeySpec(apiSecret.toByteArray(), "HmacSHA256"))
        val hash = doFinal(someString.toByteArray())
        // Convert the hash to a lowercase hex string
        hexString = hash.joinToString("") { "%02x".format(it) }
      }

      try {
        val client = HttpClient.newHttpClient()

        val req =
            HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("x-signature", hexString)
                .header("x-timestamp", timestamp)
                .header("x-api-key", apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build()

        val response = client.send(req, java.net.http.HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() == 200) {
            TrivyNotificationGroup.notifyInformation(project, "Credentials are valid.")
          return true
        }
      } catch (e: Exception) {
        TrivyNotificationGroup.notifyError(project, "Failed to validate credentials")
        return false
      }
      TrivyNotificationGroup.notifyError(project, "Failed to validate credentials")
      return false
    }
  }
}

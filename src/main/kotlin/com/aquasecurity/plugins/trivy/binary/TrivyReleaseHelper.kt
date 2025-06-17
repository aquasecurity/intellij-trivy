package com.aquasecurity.plugins.trivy.binary

import com.google.gson.JsonParser
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun getLatestGithubTag(): String? {
    val url = "https://api.github.com/repos/aquasecurity/trivy/tags"
    val client = HttpClient.newHttpClient()

    val request =
        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Accept", "application/vnd.github.v3+json")
            .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofInputStream())

    if (response.statusCode() != 200) {
        throw Exception("Failed to fetch tags from GitHub: ${response.statusCode()}")
    }

    val body = response.body().bufferedReader().use { it.readText() }
    val jsonArray = JsonParser.parseString(body).asJsonArray

    if (jsonArray.size() == 0) {
        throw Exception("No tags found in the response")
    }

    // Return the first tag name (assumed to be the latest)
    return jsonArray[0].asJsonObject["name"].asString.trimStart('v')
}

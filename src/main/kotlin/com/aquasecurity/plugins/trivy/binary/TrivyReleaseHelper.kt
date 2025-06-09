package com.aquasecurity.plugins.trivy.binary

import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request

fun getLatestGithubTag(): String? {
    val url = "https://api.github.com/repos/aquasecurity/trivy/tags"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .header("Accept", "application/vnd.github.v3+json")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            throw Exception("Unexpected code $response")
        }

        val body = response.body?.string() ?: return null
        val jsonArray = JsonParser.parseString(body).asJsonArray

        if (jsonArray.size() == 0) {
            throw Exception("No tags found in the response")
        }

        // Return the first tag name (assumed to be the latest)
        return jsonArray[0].asJsonObject["name"].asString.trimStart('v')
    }
}
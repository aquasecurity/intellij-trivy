package com.aquasecurity.plugins.trivy.settings

object Regions {
  fun getEnvUrls(region: String): Pair<String, String> {
    return when (region) {
      "Dev" ->
          Pair(
              "https://stage.api.cloudsploit.com", "https://api.dev.supply-chain.cloud.aquasec.com")

      "EU" ->
          Pair("https://eu-1.api.cloudsploit.com", "https://api.eu-1.supply-chain.cloud.aquasec.com")

      "Singapore" ->
          Pair(
              "https://ap-1.api.cloudsploit.com", "https://api.ap-1.supply-chain.cloud.aquasec.com")

      "Sydney" ->
          Pair(
              "https://ap-2.api.cloudsploit.com", "https://api.ap-2.supply-chain.cloud.aquasec.com")
      else -> Pair("https://api.cloudsploit.com", "https://api.supply-chain.cloud.aquasec.com")
    }
  }
}

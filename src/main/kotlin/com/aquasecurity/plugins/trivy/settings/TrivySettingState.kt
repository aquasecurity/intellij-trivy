package com.aquasecurity.plugins.trivy.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


@State(name = "com.aquasecurity.plugins.trivy.settings.TrivySettingState", storages = [Storage("trivy.xml")])
class TrivySettingState : PersistentStateComponent<TrivySettingState?> {
    var trivyPath: String = "trivy"
    var criticalSeverity: Boolean = true
    var highSeverity: Boolean = true
    var mediumSeverity: Boolean = true
    var lowSeverity: Boolean = true
    var unknownSeverity: Boolean = true
    var offlineScan: Boolean = false
    var ignoreUnfixed: Boolean = false
    var scanForSecrets: Boolean = false
    var scanForMisconfigurations: Boolean = true
    var scanForVulnerabilities: Boolean = true

    override fun getState(): TrivySettingState {
        return this
    }

    override fun loadState(state: TrivySettingState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: TrivySettingState
            get() = ApplicationManager.getApplication().getService(
                TrivySettingState::class.java
            )
    }
}
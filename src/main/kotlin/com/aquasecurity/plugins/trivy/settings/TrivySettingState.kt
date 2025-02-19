package com.aquasecurity.plugins.trivy.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


@State(name = "com.aquasecurity.plugins.trivy.settings.TrivySettingState", storages = [Storage("trivy.xml")])
class TrivySettingState : PersistentStateComponent<TrivySettingState?> {
    var TrivyPath: String = "trivy"
    var CriticalSeverity: Boolean = true
    var HighSeverity: Boolean = true
    var MediumSeverity: Boolean = true
    var LowSeverity: Boolean = true
    var UnknownSeverity: Boolean = true
    var OfflineScan: Boolean = false
    var IgnoreUnfixed: Boolean = false
    var SecretScanning: Boolean = false

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
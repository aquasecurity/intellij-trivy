package com.aquasecurity.plugins.trivy.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.aquasecurity.plugins.trivy.settings.TrivyProjectSettingState",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)])
@Service(Service.Level.PROJECT)
class TrivyProjectSettingState : PersistentStateComponent<TrivyProjectSettingState?> {
  var configPath: String = ""
  var useConfig: Boolean = false
  var ignorePath: String = ""
  var useIgnore: Boolean = false
  var useAquaPlatform: Boolean = false
  var enableDotNetProject: Boolean = false
  var enableGradle: Boolean = false
  var enablePackageJson: Boolean = false
  var enableSASTScanning: Boolean = true
  var skipDirList: List<String> = listOf(
    ".build",
    ".dart_tool",
    ".egg-info",
    ".egg",
    ".git",
    ".hg",
    ".svn",
    ".venv",
    ".whl",
    "bin",
    "build",
    "deps",
    "node_modules",
    "obj",
    "pods",
    "target",
    "vendor",
    "venv"
  )

  override fun getState(): TrivyProjectSettingState {
    return this
  }

  override fun loadState(state: TrivyProjectSettingState) {
    XmlSerializerUtil.copyBean(state, this)
  }

  companion object {
    fun getInstance(project: Project): TrivyProjectSettingState {
      return project.getService(TrivyProjectSettingState::class.java)
    }
  }
}

package ru.mvrlrd.relauncher.domain.repository

import ru.mvrlrd.relauncher.domain.model.AppInfo

interface AppRepository {
    fun getInstalledApps(): List<AppInfo>
    fun findByName(name: String): AppInfo?
    fun launch(app: AppInfo): Boolean
}

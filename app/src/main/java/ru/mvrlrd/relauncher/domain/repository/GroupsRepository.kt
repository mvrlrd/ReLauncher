package ru.mvrlrd.relauncher.domain.repository

import ru.mvrlrd.relauncher.domain.model.GroupApp
import ru.mvrlrd.relauncher.domain.model.GroupDir

interface GroupsRepository {
    fun getAll(): List<GroupDir>
    fun add(dir: GroupDir)
    fun getApps(): List<GroupApp>
    fun addApp(app: GroupApp)
}

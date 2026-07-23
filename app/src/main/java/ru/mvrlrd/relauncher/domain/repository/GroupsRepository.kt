package ru.mvrlrd.relauncher.domain.repository

import ru.mvrlrd.relauncher.domain.model.GroupDir

interface GroupsRepository {
    fun getAll(): List<GroupDir>
    fun add(dir: GroupDir)
}

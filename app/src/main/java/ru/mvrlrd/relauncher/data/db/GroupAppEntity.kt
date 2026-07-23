package ru.mvrlrd.relauncher.data.db

import androidx.room.Entity

@Entity(tableName = "group_apps", primaryKeys = ["groupPath", "packageName"])
data class GroupAppEntity(
    val groupPath: String,
    val packageName: String,
)

package ru.mvrlrd.relauncher.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_dirs")
data class GroupDirEntity(
    @PrimaryKey val path: String,
    val name: String,
    val parentPath: String,
)

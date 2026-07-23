package ru.mvrlrd.relauncher.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GroupDirEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDirDao(): GroupDirDao
}

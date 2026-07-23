package ru.mvrlrd.relauncher.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GroupDirEntity::class, GroupAppEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDirDao(): GroupDirDao
    abstract fun groupAppDao(): GroupAppDao
}

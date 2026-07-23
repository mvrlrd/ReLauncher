package ru.mvrlrd.relauncher.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GroupAppDao {
    @Query("SELECT * FROM group_apps")
    fun getAll(): List<GroupAppEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(e: GroupAppEntity)
}

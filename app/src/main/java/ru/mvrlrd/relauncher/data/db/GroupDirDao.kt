package ru.mvrlrd.relauncher.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GroupDirDao {
    @Query("SELECT * FROM group_dirs")
    fun getAll(): List<GroupDirEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dir: GroupDirEntity)
}

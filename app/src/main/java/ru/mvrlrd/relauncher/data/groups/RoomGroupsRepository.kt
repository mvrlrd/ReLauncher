package ru.mvrlrd.relauncher.data.groups

import ru.mvrlrd.relauncher.data.db.GroupAppDao
import ru.mvrlrd.relauncher.data.db.GroupAppEntity
import ru.mvrlrd.relauncher.data.db.GroupDirDao
import ru.mvrlrd.relauncher.data.db.GroupDirEntity
import ru.mvrlrd.relauncher.domain.model.GroupApp
import ru.mvrlrd.relauncher.domain.model.GroupDir
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomGroupsRepository @Inject constructor(
    private val dirDao: GroupDirDao,
    private val appDao: GroupAppDao,
) : GroupsRepository {

    override fun getAll(): List<GroupDir> =
        dirDao.getAll().map { GroupDir(it.path, it.name, it.parentPath) }

    override fun add(dir: GroupDir) {
        dirDao.insert(GroupDirEntity(dir.path, dir.name, dir.parentPath))
    }

    override fun getApps(): List<GroupApp> =
        appDao.getAll().map { GroupApp(it.groupPath, it.packageName) }

    override fun addApp(app: GroupApp) {
        appDao.insert(GroupAppEntity(app.groupPath, app.packageName))
    }
}

package ru.mvrlrd.relauncher.data.groups

import ru.mvrlrd.relauncher.data.db.GroupDirDao
import ru.mvrlrd.relauncher.data.db.GroupDirEntity
import ru.mvrlrd.relauncher.domain.model.GroupDir
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomGroupsRepository @Inject constructor(
    private val dao: GroupDirDao,
) : GroupsRepository {

    override fun getAll(): List<GroupDir> =
        dao.getAll().map { GroupDir(it.path, it.name, it.parentPath) }

    override fun add(dir: GroupDir) {
        dao.insert(GroupDirEntity(dir.path, dir.name, dir.parentPath))
    }
}

package ru.mvrlrd.relauncher.domain.usecase

import ru.mvrlrd.relauncher.domain.model.GroupDir
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import javax.inject.Inject

class CreateGroupDirUseCase @Inject constructor(
    private val repository: GroupsRepository,
) {
    operator fun invoke(dir: GroupDir) = repository.add(dir)
}

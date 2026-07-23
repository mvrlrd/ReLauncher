package ru.mvrlrd.relauncher.domain.usecase

import ru.mvrlrd.relauncher.domain.model.GroupApp
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import javax.inject.Inject

class LinkAppToGroupUseCase @Inject constructor(
    private val repository: GroupsRepository,
) {
    operator fun invoke(app: GroupApp) = repository.addApp(app)
}

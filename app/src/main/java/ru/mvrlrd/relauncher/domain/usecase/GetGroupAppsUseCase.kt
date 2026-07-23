package ru.mvrlrd.relauncher.domain.usecase

import ru.mvrlrd.relauncher.domain.model.GroupApp
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import javax.inject.Inject

class GetGroupAppsUseCase @Inject constructor(
    private val repository: GroupsRepository,
) {
    operator fun invoke(): List<GroupApp> = repository.getApps()
}

package ru.mvrlrd.relauncher.domain.usecase

import ru.mvrlrd.relauncher.domain.model.AppInfo
import ru.mvrlrd.relauncher.domain.repository.AppRepository
import javax.inject.Inject

class GetInstalledAppsUseCase @Inject constructor(
    private val repository: AppRepository,
) {
    operator fun invoke(): List<AppInfo> = repository.getInstalledApps()
}

package ru.mvrlrd.relauncher.domain.usecase

import ru.mvrlrd.relauncher.domain.model.AppInfo
import ru.mvrlrd.relauncher.domain.repository.AppRepository
import javax.inject.Inject

class FindAppUseCase @Inject constructor(
    private val repository: AppRepository,
) {
    operator fun invoke(name: String): AppInfo? = repository.findByName(name)
}

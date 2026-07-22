package ru.mvrlrd.relauncher.domain.usecase

import ru.mvrlrd.relauncher.domain.repository.AppRepository
import javax.inject.Inject

class LaunchAppUseCase @Inject constructor(
    private val repository: AppRepository,
) {
    sealed interface Result {
        data class Launched(val label: String) : Result
        data class NotFound(val name: String) : Result
        data class NoLaunchIntent(val label: String) : Result
    }

    operator fun invoke(name: String): Result {
        val app = repository.findByName(name) ?: return Result.NotFound(name)
        return if (repository.launch(app)) {
            Result.Launched(app.label)
        } else {
            Result.NoLaunchIntent(app.label)
        }
    }
}

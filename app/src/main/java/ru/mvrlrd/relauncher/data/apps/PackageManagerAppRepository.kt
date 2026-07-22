package ru.mvrlrd.relauncher.data.apps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import ru.mvrlrd.relauncher.domain.model.AppInfo
import ru.mvrlrd.relauncher.domain.repository.AppRepository
import ru.mvrlrd.relauncher.domain.resolveAppByName
import ru.mvrlrd.relauncher.domain.slugify
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PackageManagerAppRepository @Inject constructor(
    private val context: Context,
) : AppRepository {

    private val packageManager: PackageManager get() = context.packageManager

    private val cache: List<AppInfo> by lazy { loadApps() }

    override fun getInstalledApps(): List<AppInfo> = cache

    override fun findByName(name: String): AppInfo? = resolveAppByName(cache, name)

    override fun launch(app: AppInfo): Boolean {
        val intent = packageManager.getLaunchIntentForPackage(app.packageName) ?: return false
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        return true
    }

    private fun loadApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolved = packageManager.queryIntentActivities(intent, 0)
        val usedSlugs = HashSet<String>()
        return resolved
            .map { it.activityInfo }
            .distinctBy { it.packageName }
            .map { info ->
                val label = info.loadLabel(packageManager).toString()
                val base = slugify(label).ifEmpty { info.packageName }
                val slug = uniqueSlug(base, info.packageName, usedSlugs)
                AppInfo(packageName = info.packageName, label = label, slug = slug)
            }
            .sortedBy { it.label.lowercase() }
    }

    private fun uniqueSlug(base: String, packageName: String, used: HashSet<String>): String {
        if (used.add(base)) return base
        val fallback = slugify(packageName).ifEmpty { packageName }
        if (used.add(fallback)) return fallback
        var i = 2
        while (!used.add("${base}_$i")) i++
        return "${base}_$i"
    }
}

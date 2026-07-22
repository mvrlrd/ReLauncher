package ru.mvrlrd.relauncher.domain

import ru.mvrlrd.relauncher.domain.model.AppInfo

/** Pure lookup of an app by slug or package name over a known list. */
fun resolveAppByName(apps: List<AppInfo>, name: String): AppInfo? =
    apps.firstOrNull { it.slug == name } ?: apps.firstOrNull { it.packageName == name }

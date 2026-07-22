package ru.mvrlrd.relauncher.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.mvrlrd.relauncher.domain.model.AppInfo

class AppNameResolveTest {

    private val apps = listOf(
        AppInfo(packageName = "org.telegram.messenger", label = "Telegram", slug = "telegram"),
        AppInfo(packageName = "com.google.android.apps.maps", label = "Google Maps", slug = "google_maps"),
    )

    @Test
    fun resolvesBySlug() {
        assertEquals("org.telegram.messenger", resolveAppByName(apps, "telegram")?.packageName)
    }

    @Test
    fun resolvesByPackageName() {
        assertEquals("google_maps", resolveAppByName(apps, "com.google.android.apps.maps")?.slug)
    }

    @Test
    fun returnsNullForUnknown() {
        assertNull(resolveAppByName(apps, "unknown"))
    }

    @Test
    fun slugTakesPriorityOverPackage() {
        val collided = listOf(
            AppInfo(packageName = "shared", label = "First", slug = "first"),
            AppInfo(packageName = "second", label = "Second", slug = "shared"),
        )
        assertEquals("Second", resolveAppByName(collided, "shared")?.label)
    }
}

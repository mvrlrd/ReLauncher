package ru.mvrlrd.relauncher.vfs

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PathResolverTest {

    private val tree = VfsTree()
    private val root = tree.root

    @Test
    fun resolvesAbsolutePath() {
        val node = PathResolver.resolve(root, "/apps")
        assertEquals("/apps", node?.path)
    }

    @Test
    fun resolvesRelativePathFromCwd() {
        val apps = root.child("apps")!!
        val node = PathResolver.resolve(apps, ".")
        assertEquals("/apps", node?.path)
    }

    @Test
    fun dotDotGoesUp() {
        val apps = root.child("apps")!!
        val node = PathResolver.resolve(apps, "..")
        assertEquals("/", node?.path)
    }

    @Test
    fun dotDotAtRootStaysAtRoot() {
        val node = PathResolver.resolve(root, "..")
        assertEquals("/", node?.path)
        val chained = PathResolver.resolve(root, "../../apps")
        assertEquals("/apps", chained?.path)
    }

    @Test
    fun missingSegmentReturnsNull() {
        assertNull(PathResolver.resolve(root, "/nope"))
        assertNull(PathResolver.resolve(root, "apps/nope"))
    }

    @Test
    fun emptyPathResolvesToCwd() {
        val apps = root.child("apps")!!
        assertEquals("/apps", PathResolver.resolve(apps, "")?.path)
    }
}

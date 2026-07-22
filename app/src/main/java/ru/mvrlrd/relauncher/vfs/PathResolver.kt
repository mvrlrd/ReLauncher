package ru.mvrlrd.relauncher.vfs

/** Resolves absolute/relative paths against a current directory. */
object PathResolver {

    /**
     * Resolves [path] starting from [cwd].
     * Supports absolute (`/...`) and relative paths, `.` and `..` segments.
     * `..` at the root stays at the root. Returns `null` for a missing segment.
     */
    fun resolve(cwd: VfsNode, path: String): VfsNode? {
        var node = if (path.startsWith("/")) root(cwd) else cwd
        val segments = path.split("/").filter { it.isNotEmpty() }
        for (segment in segments) {
            node = when (segment) {
                "." -> node
                ".." -> node.parent ?: node
                else -> node.child(segment) ?: return null
            }
        }
        return node
    }

    private fun root(node: VfsNode): VfsNode {
        var current = node
        while (!current.isRoot) current = current.parent!!
        return current
    }
}

package ru.mvrlrd.relauncher.vfs

/** A directory node of the virtual file system. */
class VfsNode(
    val name: String,
    val parent: VfsNode? = null,
) {
    private val _children = LinkedHashMap<String, VfsNode>()

    val children: Map<String, VfsNode> get() = _children

    val isRoot: Boolean get() = parent == null

    /** Absolute path from the root, e.g. `/apps`. Root is `/`. */
    val path: String
        get() {
            if (isRoot) return "/"
            val segments = ArrayDeque<String>()
            var node: VfsNode? = this
            while (node != null && !node.isRoot) {
                segments.addFirst(node.name)
                node = node.parent
            }
            return "/" + segments.joinToString("/")
        }

    fun child(name: String): VfsNode? = _children[name]

    fun addChild(name: String): VfsNode {
        val node = VfsNode(name, this)
        _children[name] = node
        return node
    }
}

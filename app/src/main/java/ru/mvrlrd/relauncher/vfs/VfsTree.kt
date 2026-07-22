package ru.mvrlrd.relauncher.vfs

/** Virtual file system tree with a root and static top-level branches. */
class VfsTree {
    val root: VfsNode = VfsNode(name = "")

    init {
        root.addChild("apps")
        root.addChild("settings")
    }
}

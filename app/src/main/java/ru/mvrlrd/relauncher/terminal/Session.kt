package ru.mvrlrd.relauncher.terminal

import ru.mvrlrd.relauncher.vfs.VfsNode
import ru.mvrlrd.relauncher.vfs.VfsTree

/** Holds mutable terminal session state: current working directory. */
class Session(val tree: VfsTree = VfsTree()) {
    var cwd: VfsNode = tree.root
}

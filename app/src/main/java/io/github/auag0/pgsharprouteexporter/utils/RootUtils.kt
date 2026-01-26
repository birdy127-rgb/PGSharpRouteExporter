package io.github.auag0.pgsharprouteexporter.utils

import com.topjohnwu.superuser.Shell

object RootUtils {
    fun checkRoot(): Boolean {
        try {
            val shell = Shell.getShell()
            if (shell.isRoot) return true
            shell.close()
            return Shell.getShell().isRoot
        } catch (_: Exception) {
            return false
        }
    }
}
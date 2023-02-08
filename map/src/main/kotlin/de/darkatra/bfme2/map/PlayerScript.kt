package de.darkatra.bfme2.map

// TODO: find a better name for this (it's basically a ScriptFolder, but you can't disable or name it) e.g. "name", "active" and "subroutine" data is missing
data class PlayerScript(
    val scriptFolders: List<ScriptFolder>,
    val scripts: List<Script>
)

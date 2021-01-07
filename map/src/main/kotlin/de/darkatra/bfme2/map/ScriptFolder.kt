package de.darkatra.bfme2.map

/**
 * A folder containing Scripts or even more Folders.
 * It is possible to enable and disable the folder, which results in disabling all content as well.
 */
data class ScriptFolder(
	val scriptFolders: List<ScriptFolder>,
	val scripts: List<Script>,
	val name: String,
	val active: Boolean,
	val subroutine: Boolean // TODO: is this a setting in the worldbuilder?
)

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
) {

    class Builder {
        private var scriptFolders: List<ScriptFolder>? = null
        private var scripts: List<Script>? = null
        private var name: String? = null
        private var active: Boolean? = null
        private var subroutine: Boolean? = null

        fun scriptFolders(scriptFolders: List<ScriptFolder>) = apply { this.scriptFolders = scriptFolders }
        fun scripts(scripts: List<Script>) = apply { this.scripts = scripts }
        fun name(name: String) = apply { this.name = name }
        fun active(active: Boolean) = apply { this.active = active }
        fun subroutine(subroutine: Boolean) = apply { this.subroutine = subroutine }

        fun build() = ScriptFolder(
            scriptFolders = scriptFolders ?: throwIllegalStateExceptionForField("scriptFolders"),
            scripts = scripts ?: throwIllegalStateExceptionForField("scripts"),
            name = name ?: throwIllegalStateExceptionForField("name"),
            active = active ?: throwIllegalStateExceptionForField("active"),
            subroutine = subroutine ?: throwIllegalStateExceptionForField("subroutine")
        )

        private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
            throw IllegalStateException("Field '$fieldName' is null.")
        }
    }
}

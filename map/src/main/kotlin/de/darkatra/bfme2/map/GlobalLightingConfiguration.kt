package de.darkatra.bfme2.map

data class GlobalLightingConfiguration(
    val terrainSun: GlobalLight,
    val terrainAccent1: GlobalLight,
    val terrainAccent2: GlobalLight,
    val objectSun: GlobalLight?,
    val objectAccent1: GlobalLight?,
    val objectAccent2: GlobalLight?,
    val infantrySun: GlobalLight?,
    val infantryAccent1: GlobalLight?,
    val infantryAccent2: GlobalLight?
) {

    class Builder {
        private var terrainSun: GlobalLight? = null
        private var terrainAccent1: GlobalLight? = null
        private var terrainAccent2: GlobalLight? = null
        private var objectSun: GlobalLight? = null
        private var objectAccent1: GlobalLight? = null
        private var objectAccent2: GlobalLight? = null
        private var infantrySun: GlobalLight? = null
        private var infantryAccent1: GlobalLight? = null
        private var infantryAccent2: GlobalLight? = null

        fun terrainSun(terrainSun: GlobalLight) = apply { this.terrainSun = terrainSun }
        fun terrainAccent1(terrainAccent1: GlobalLight) = apply { this.terrainAccent1 = terrainAccent1 }
        fun terrainAccent2(terrainAccent2: GlobalLight) = apply { this.terrainAccent2 = terrainAccent2 }
        fun objectSun(objectSun: GlobalLight) = apply { this.objectSun = objectSun }
        fun objectAccent1(objectAccent1: GlobalLight) = apply { this.objectAccent1 = objectAccent1 }
        fun objectAccent2(objectAccent2: GlobalLight) = apply { this.objectAccent2 = objectAccent2 }
        fun infantrySun(infantrySun: GlobalLight) = apply { this.infantrySun = infantrySun }
        fun infantryAccent1(infantryAccent1: GlobalLight) = apply { this.infantryAccent1 = infantryAccent1 }
        fun infantryAccent2(infantryAccent2: GlobalLight) = apply { this.infantryAccent2 = infantryAccent2 }

        fun build() = GlobalLightingConfiguration(
            terrainSun = terrainSun ?: throwIllegalStateExceptionForField("terrainSun"),
            terrainAccent1 = terrainAccent1 ?: throwIllegalStateExceptionForField("terrainAccent1"),
            terrainAccent2 = terrainAccent2 ?: throwIllegalStateExceptionForField("terrainAccent2"),
            objectSun = objectSun,
            objectAccent1 = objectAccent1,
            objectAccent2 = objectAccent2,
            infantrySun = infantrySun,
            infantryAccent1 = infantryAccent1,
            infantryAccent2 = infantryAccent2
        )

        private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
            throw IllegalStateException("Field '$fieldName' is null.")
        }
    }
}

package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class Skybox(
	val position: Vector3,
	val scale: Float,
	val rotation: Float, // FIXME: apparently not a little endian float
	// TODO: this is probably not a string, figure out the correct format and parse it
	val textureScheme: String
)

package de.darkatra.bfme2.map.globallighting

data class GlobalLightingConfiguration(
    val terrainSun: GlobalLight,
    val terrainAccent1: GlobalLight,
    val terrainAccent2: GlobalLight,
    val objectSun: GlobalLight,
    val objectAccent1: GlobalLight,
    val objectAccent2: GlobalLight,
    val infantrySun: GlobalLight,
    val infantryAccent1: GlobalLight,
    val infantryAccent2: GlobalLight
)

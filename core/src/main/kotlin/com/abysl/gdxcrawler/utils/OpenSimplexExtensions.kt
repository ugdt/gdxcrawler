package com.abysl.gdxcrawler.utils

import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2

fun OpenSimplex2S.noise2(vector2: Vector2): Double {
    return noise2(vector2.x, vector2.y)
}

fun OpenSimplex2S.noise2(gridPoint2: GridPoint2): Double {
    return noise2(gridPoint2.x, gridPoint2.y)
}

fun OpenSimplex2S.noise2(x: Int, y: Int): Double {
    return noise2(x.toDouble(), y.toDouble())
}

fun OpenSimplex2S.noise2(x: Float, y: Float): Double {
    return noise2(x.toDouble(), y.toDouble())
}
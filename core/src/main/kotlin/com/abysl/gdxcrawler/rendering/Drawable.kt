package com.abysl.gdxcrawler.rendering

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.GridPoint2

interface Drawable {
    var depth: Int
    fun draw(batch: SpriteBatch, x: Float, y: Float, width: Float, height: Float)
}
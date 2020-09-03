package com.abysl.gdxcrawler.rendering

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface Drawable {
    var depth: Int
    fun draw(batch: SpriteBatch, x: Float, y: Float, width: Float, height: Float)
}

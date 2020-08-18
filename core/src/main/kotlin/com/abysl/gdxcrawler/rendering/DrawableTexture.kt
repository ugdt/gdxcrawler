package com.abysl.gdxcrawler.rendering

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.graphics.use

class DrawableTexture(private val texture: Texture, override var depth: Int) : Drawable {
    override fun draw(batch: SpriteBatch, x: Float, y: Float, width: Float, height: Float) {
        batch.use {
            it.draw(texture, x, y, width, height)
        }
    }
}
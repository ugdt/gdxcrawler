package com.abysl.gdxcrawler

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.graphics.use

class LoadingScreen : Screen{
    val batch = SpriteBatch()
    val font = BitmapFont()

    override fun hide() {

    }

    override fun show() {
    }

    override fun render(delta: Float) {
        batch.use {
            font.draw(it, "Loading", 100f, 100f)
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }
}

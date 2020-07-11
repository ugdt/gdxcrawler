package com.abysl.gdxcrawler

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.graphics.use

class MainMenu : Screen {

    val texture = TextureAtlas()
    val sprite = Sprite()
    val batch = SpriteBatch()

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        batch.use {

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
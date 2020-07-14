package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.utils.PixelPerfectScreen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import ktx.math.*

class TestScreen : IPhysics, PixelPerfectScreen(320, 180) {
    private val testSprite = Texture("testboard.png")
    private var direction = Vector2(1f, 1f)

    override fun hide() {
    }

    override fun show() {
    }

    override fun draw(delta: Float)
    {
        for (y in 0 until baseHeight step 16)
            for (x in 0 until baseWidth step 32) {
                if ((y / 16) % 2 > 0) {
                    fboSpriteBatch.draw(testSprite, x.toFloat() + 16, y.toFloat())
                } else {
                    fboSpriteBatch.draw(testSprite, x.toFloat(), y.toFloat())
                }
            }

    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

    override fun physics(delta: Float) {
    }
}

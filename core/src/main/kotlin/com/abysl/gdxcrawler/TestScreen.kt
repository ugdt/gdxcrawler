package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.utils.PixelPerfectRenderer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

class TestScreen : Screen {
    private val testSprite = Texture("testboard.png")
    private var direction = Vector2(1f, 1f)
    private val baseWidth = 320
    private val baseHeight = 180
    private val pixelPerfectRenderer = PixelPerfectRenderer(baseWidth, baseHeight)

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float)
    {
        pixelPerfectRenderer.render { spriteBatch ->
            for (y in 0 until baseHeight step 16) {
                for (x in 0 until baseWidth step 32) {
                    if ((y / 16) % 2 > 0) {
                        spriteBatch.draw(testSprite, x.toFloat() + 16, y.toFloat())
                    } else {
                        spriteBatch.draw(testSprite, x.toFloat(), y.toFloat())
                    }
                }
            }
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        pixelPerfectRenderer.resize(width, height)
    }

    override fun dispose() {
    }
}

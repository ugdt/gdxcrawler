package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.entities.PlayerEntity
import com.abysl.gdxcrawler.utils.IPhysics
import com.abysl.gdxcrawler.utils.PixelPerfectScreen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import ktx.math.*

class TestScreen : IPhysics, PixelPerfectScreen(320, 180) {
    private val testSprite = Texture("testboard.png")
    private val player = PlayerEntity()
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
                    spriteBatch.draw(testSprite, x.toFloat() + 16, y.toFloat())
                } else {
                    spriteBatch.draw(testSprite, x.toFloat(), y.toFloat())
                }
            }

        spriteBatch.draw(player.texture, player.position.x, player.position.y)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

    override fun physics(delta: Float) {
        val speed = 20f

        val currentPlayerPosition = player.position

        val maxWidth = baseWidth - 16f
        val maxHeight = baseHeight - 16f

        if (currentPlayerPosition.x !in 0f..maxWidth) {
            direction.x = -direction.x
        }
        if (currentPlayerPosition.y !in 0f..maxHeight) {
            direction.y = -direction.y
        }

        val newPlayerPosition = currentPlayerPosition + (direction * 2 * delta * speed)
        player.position = newPlayerPosition
    }
}

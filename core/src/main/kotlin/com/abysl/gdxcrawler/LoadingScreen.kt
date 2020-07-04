package com.abysl.gdxcrawler

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.graphics.use

class LoadingScreen : Screen{
    val batch = SpriteBatch()
    val font = BitmapFont()

    var x: Float = 0f
    var y: Float = 0f

    var counter = 0f
    var timer = 0f
    var fps = 0f

    override fun hide() {

    }

    override fun show() {
    }

    override fun render(delta: Float) {
        counter++
        timer += (delta)
        fps = (counter / delta)

        println(fps)
        batch.use {
            font.draw(it, "$fps", x, y)
        }

    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        x = width / 2f
        y = height / 2f
    }

    override fun dispose() {
    }
}

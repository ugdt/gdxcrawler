package com.abysl.gdxcrawler.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.graphics.use

abstract class PixelPerfectScreen(protected val baseWidth: Int, protected val baseHeight: Int) : Screen {
    protected val spriteBatch = SpriteBatch()
    private val frameBuffer: FrameBuffer = FrameBuffer(Pixmap.Format.RGBA8888, baseWidth, baseHeight, false)
    private val viewport = IntFitViewport(baseWidth.toFloat(), baseHeight.toFloat())

    override fun render(delta: Float) {
        frameBuffer.use { _ ->
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            Gdx.gl.glEnable(GL20.GL_BLEND)

            spriteBatch.use {
                draw(delta)
            }
        }

        viewport.apply(true)

        val texture = frameBuffer.colorBufferTexture
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)

        spriteBatch.use {
            it.draw(texture, 0f, 0f)
        }
    }

    override fun resize(newWindowWidth: Int, newWindowHeight: Int) {
        viewport.update(newWindowWidth, newWindowHeight, true)
    }

    abstract fun draw(delta: Float)
}
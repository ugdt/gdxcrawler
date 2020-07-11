package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.utils.IntFitViewport
import com.abysl.gdxcrawler.utils.StaticViewport
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.graphics.use

class LoadingScreen : Screen {
    private val batch = SpriteBatch()
    private var worldBuffer: FrameBuffer
    private val baseWidth = 320
    private val baseHeight = 180
    private var scale = 1
    private val tileSprite = Texture("testboard.png")
    private val staticViewPort: Viewport

    init {
        worldBuffer = FrameBuffer(Pixmap.Format.RGBA8888, baseWidth, baseHeight, false)
        staticViewPort = StaticViewport(baseWidth.toFloat(), baseHeight.toFloat())
    }

    override fun hide() {

    }

    override fun show() {
    }

    override fun render(delta: Float)
    {
        worldBuffer.use { _ ->
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            Gdx.gl.glEnable(GL20.GL_BLEND)

            batch.use {
                for (y in 0 until baseHeight step 16)
                for (x in 0 until baseWidth step 32) {
                    if ((y / 16) % 2 > 0) {
                        it.draw(tileSprite, x.toFloat() + 16, y.toFloat())
                    } else {
                        it.draw(tileSprite, x.toFloat(), y.toFloat())
                    }
                }
            }
        }

        val texture = worldBuffer.colorBufferTexture
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)

        batch.use {
            it.draw(texture, 0f, 0f)
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        //val sourceRatio = height.toFloat() / width
        //val baseRatio = baseHeight.toFloat() / baseWidth
        //scale = if (baseRatio > sourceRatio) baseWidth / width else baseHeight / height
        val widthScale = width / baseWidth
        val heightScale = height / baseHeight

        scale = widthScale.coerceAtMost(heightScale)

        staticViewPort.update(width, height)
        worldBuffer = FrameBuffer(Pixmap.Format.RGBA8888, baseWidth * scale, baseHeight * scale, false)
    }

    override fun dispose() {
    }
}

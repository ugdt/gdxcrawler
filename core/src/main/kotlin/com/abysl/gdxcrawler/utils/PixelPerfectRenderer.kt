package com.abysl.gdxcrawler.utils

import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.graphics.*

class PixelPerfectRenderer(baseWidth: Float, baseHeight: Float, tileSize: Int) {
    private val spriteBatch = SpriteBatch()
    private val camera = OrthographicCamera(baseWidth, baseHeight)

    fun render(world: World) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.projectionMatrix = camera.combined
        camera.update()

        spriteBatch.use {

        }
        Gdx.app.postRunnable {
            if (Gdx.graphics.width % 2 != 0) {
                Gdx.graphics.setWindowedMode(Gdx.graphics.width - 1, Gdx.graphics.height)
            }
            if (Gdx.graphics.height % 2 != 0) {
                Gdx.graphics.setWindowedMode(Gdx.graphics.width, Gdx.graphics.height - 1)
            }
        }
    }
}

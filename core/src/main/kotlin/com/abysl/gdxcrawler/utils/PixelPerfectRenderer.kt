package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.components.CPhysics
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.components.CTexture
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.graphics.use
import kotlin.math.roundToInt

class PixelPerfectRenderer(val world: World, val baseWidth: Float, val baseHeight: Float, val tileSize: Int) {
    private val spriteBatch = SpriteBatch()
    private val cam: OrthographicCamera = world.getRegistered(OrthographicCamera::class.java)
            ?: OrthographicCamera(baseWidth, baseHeight)
    private val tagManager = world.getSystem(TagManager::class.java)


    fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        val player = tagManager.getEntity("PLAYER")
        val pos = player.getComponent(CPosition::class.java).position
        cam.position.set(pos.x, pos.y, 0f)
        cam.update()
        spriteBatch.projectionMatrix = cam.combined
        spriteBatch.use {
            it.draw(player.getComponent(CTexture::class.java).texture, 0f, 0f, 1f, 1f)
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

    fun resize(width: Int, height: Int) {
        // calculate the scale that gets closest to showing 20.0 x 11.25 tiles on the screen
        val widthScale = (width.toFloat() / (baseWidth * tileSize)).roundToInt().coerceAtLeast(1)
        val heightScale = (height.toFloat() / (baseHeight * tileSize)).roundToInt().coerceAtLeast(1)
        val scale: Int = maxOf(widthScale, heightScale)
        println(scale)
        // scale the tiles by that amount
        val tileFactor: Double = scale.toDouble() * tileSize
        // calculate the camera width needed with the proper scale to keep things pixel perfect
        val widthMod: Double = (width.toDouble() - (baseWidth * tileFactor)) / tileFactor
        // calculate the camera height needed to keep things pixel perfect
        val heightMod: Double = (height.toDouble() - (baseHeight * tileFactor)) / tileFactor
        cam.viewportWidth = (baseWidth + widthMod).toFloat()
        cam.viewportHeight = (baseHeight + heightMod).toFloat()
    }
}

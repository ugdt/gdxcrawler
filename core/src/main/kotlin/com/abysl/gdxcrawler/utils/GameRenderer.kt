package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.components.CTexture
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import ktx.graphics.use
import kotlin.math.roundToInt

class GameRenderer(val world: World, private val baseWidth: Float, private val baseHeight: Float, private val tileSize: Int) {
    private val spriteBatch = SpriteBatch()
    private val cam: OrthographicCamera = world.getRegistered(OrthographicCamera::class.java)
            ?: OrthographicCamera(baseWidth, baseHeight)
    private val tagManager = world.getSystem(TagManager::class.java)
    private var tileMapRenderer = OrthogonalTiledMapRenderer(world.getRegistered(TiledMap::class.java), 1 / 16f)

    fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val player = tagManager.getEntity("PLAYER")
        val pos = player.getComponent(CPosition::class.java).position

        cam.position.set(pos.x, pos.y, 0f)
        cam.update()

        renderMap()

        spriteBatch.projectionMatrix = cam.combined
        spriteBatch.use {
            it.draw(player.getComponent(CTexture::class.java).texture, pos.x, pos.y, 1f, 1f)
        }
    }

    private fun renderMap() {
        tileMapRenderer.map = world.getRegistered<TiledMap>("tileMap")
        tileMapRenderer.setView(cam)
        tileMapRenderer.render()
    }

    fun resize(width: Int, height: Int) {
        // calculate the scale that gets closest to showing 20.0 x 11.25 tiles on the screen
        val widthScale: Int = (width.toFloat() / (baseWidth * tileSize)).roundToInt().coerceAtLeast(1)
        val heightScale: Int = (height.toFloat() / (baseHeight * tileSize)).roundToInt().coerceAtLeast(1)
        val scale: Int = maxOf(widthScale, heightScale)
        // scale the tiles by that amount
        val tileWidthPixels: Int = scale * tileSize
        // calculate the camera width needed with the proper scale to keep things pixel perfect

        val deltaWidth: Float = (width - (baseWidth * tileWidthPixels).roundToInt()) / tileWidthPixels.toFloat()
        // calculate the camera height needed to keep things pixel perfect
        val deltaHeight: Float = (height - (baseHeight * tileWidthPixels)) / tileWidthPixels
        cam.viewportWidth = (baseWidth + deltaWidth)
        cam.viewportHeight = (baseHeight + deltaHeight)

        if (width % 2 != 0) {
            Gdx.graphics.setWindowedMode(width - 1, height)
        }

        if (height % 2 != 0) {
            Gdx.graphics.setWindowedMode(width, height - 1)
        }
    }
}

package com.abysl.gdxcrawler.rendering

import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.components.CTexture
import com.abysl.gdxcrawler.settings.RenderSettings
import com.abysl.gdxcrawler.world.Chunk
import com.abysl.gdxcrawler.world.TileWorld
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.World
import com.artemis.managers.TagManager
import com.artemis.utils.IntBag
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import kotlin.math.roundToInt

class GameRenderer(val world: World, private val tileWorld: TileWorld, private val renderSettings: RenderSettings) {
    private val spriteBatch = SpriteBatch()
    private val cam: OrthographicCamera = world.getRegistered(OrthographicCamera::class.java)
        ?: OrthographicCamera(renderSettings.baseWidth, renderSettings.baseHeight)
    private val tagManager = world.getSystem(TagManager::class.java)
    private val drawableAspect: Aspect.Builder = Aspect.all(CTexture::class.java, CPosition::class.java)
    private val mPosition: ComponentMapper<CPosition> = world.getMapper(CPosition::class.java)
    private val mTexture: ComponentMapper<CTexture> = world.getMapper(CTexture::class.java)

    fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val player = tagManager.getEntity("PLAYER")
        val pos = player.getComponent(CPosition::class.java).position

        cam.position.set(pos.x, pos.y, 0f)
        cam.update()

        val drawables: List<Pair<Vector2, Drawable>> =
            (getEntities().map(::entityToDrawable) + tileWorld.getActiveChunks().flatMap(::chunkToDrawables))
                .sortedWith(compareBy({ it.second.depth }, { if (it.second is DrawableLayer) -1 else 1 }))
        drawables.forEach {
            spriteBatch.projectionMatrix = cam.combined
            it.second.draw(spriteBatch, it.first.x, it.first.y, 1f, 1f)
        }
    }

    private fun entityToDrawable(id: Int): Pair<Vector2, Drawable> {
        val cPosition = mPosition[id]
        return cPosition.position to DrawableTexture(mTexture[id].texture, cPosition.depth)
    }

    private fun chunkToDrawables(chunk: Chunk): List<Pair<Vector2, Drawable>> {
        val result: MutableList<Pair<Vector2, Drawable>> = mutableListOf()
        for (layer in chunk.tileMap.layers) {
            val drawable = DrawableLayer(cam, chunk.tileMap, layer as TiledMapTileLayer, renderSettings.tileSize)
            val size = chunk.size.toFloat()
            val position = Vector2(chunk.chunkPosition.x * size, chunk.chunkPosition.y * size)
            result.add(position to drawable)
        }
        return result
    }

    private fun getEntities(): List<Int> {
        val entities: IntBag = world.aspectSubscriptionManager.get(drawableAspect).entities
        val result: MutableList<Int> = mutableListOf()

        for (i in 0 until entities.size()) {
            result.add(entities.get(i))
        }

        return result
    }

    fun resize(width: Int, height: Int) {
        // calculate the scale that gets closest to showing 20.0 x 11.25 tiles on the screen
        val widthScale: Int = (width.toFloat() / (renderSettings.baseWidth * renderSettings.tileSize)).roundToInt().coerceAtLeast(1)
        val heightScale: Int = (height.toFloat() / (renderSettings.baseHeight * renderSettings.tileSize)).roundToInt().coerceAtLeast(1)
        val scale: Int = maxOf(widthScale, heightScale)
        // scale the tiles by that amount
        val tileWidthPixels: Int = scale * renderSettings.tileSize
        // calculate the camera width needed with the proper scale to keep things pixel perfect

        val deltaWidth: Float = (width - (renderSettings.baseWidth * tileWidthPixels).roundToInt()) / tileWidthPixels.toFloat()
        // calculate the camera height needed to keep things pixel perfect
        val deltaHeight: Float = (height - (renderSettings.baseHeight * tileWidthPixels)) / tileWidthPixels
        cam.viewportWidth = (renderSettings.baseWidth + deltaWidth)
        cam.viewportHeight = (renderSettings.baseHeight + deltaHeight)

        if (width % 2 != 0) {
            Gdx.graphics.setWindowedMode(width - 1, height)
        }

        if (height % 2 != 0) {
            Gdx.graphics.setWindowedMode(width, height - 1)
        }
    }
}

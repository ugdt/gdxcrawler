package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.components.CTexture
import com.abysl.gdxcrawler.world.Chunk
import com.abysl.gdxcrawler.world.TileWorld
import com.artemis.Aspect
import com.artemis.World
import com.artemis.managers.TagManager
import com.artemis.utils.IntBag
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayers
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import ktx.graphics.use
import ktx.log.info
import ktx.tiled.propertyOrNull
import kotlin.math.roundToInt

class GameRenderer(val world: World, val tileWorld: TileWorld, private val baseWidth: Float, private val baseHeight: Float, private val tileSize: Int) {
    private val spriteBatch = SpriteBatch()
    private val cam: OrthographicCamera = world.getRegistered(OrthographicCamera::class.java)
            ?: OrthographicCamera(baseWidth, baseHeight)
    private val tagManager = world.getSystem(TagManager::class.java)
    val entityAspect: Aspect.Builder = Aspect.all(CTexture::class.java, CPosition::class.java)

    fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val player = tagManager.getEntity("PLAYER")
        val pos = player.getComponent(CPosition::class.java).position

        cam.position.set(pos.x, pos.y, 0f)
        cam.update()

        val entities: IntBag = world.aspectSubscriptionManager.get(entityAspect).entities
        val chunks: List<Chunk> = tileWorld.getActiveChunks()
        spriteBatch.projectionMatrix = cam.combined
        renderChunks(chunks)
        spriteBatch.projectionMatrix = cam.combined
        renderEntities(entities)
    }

    val mPosition = world.getMapper(CPosition::class.java)
    val mTexture = world.getMapper(CTexture::class.java)

    fun renderEntities(entities: IntBag){
        spriteBatch.use {
            for (i in 0 until entities.size()) {
                val id = entities[i]
                val position = mPosition[id].position
                val cTexture = mTexture[id]
                it.draw(cTexture.texture, position.x, position.y, cTexture.width, cTexture.height)
            }
        }
    }

    fun renderChunks(chunks: List<Chunk>){
        for (chunk in chunks) {
            val renderer = OrthogonalTiledMapRenderer(chunk.tileMap, 1f / tileSize, spriteBatch)
            val size = chunk.size.toFloat()
            val c = Vector3(chunk.position.x * size, chunk.position.y * size, 0f)
            val matrix = Matrix4(cam.combined).translate(c.x, c.y, 0f)
            renderer.setView(matrix, 0f, 0f, size, size)
            renderer.render()
        }
    }

    fun renderDepth(depth: Int, chunks: List<Chunk>, entities: IntArray) {
        spriteBatch.use {
            chunks.forEach { chunk ->
                val renderer = OrthogonalTiledMapRenderer(chunk.tileMap)
                val layers = chunk.tileMap.layers
                        .filter { layer -> layer.propertyOrNull<Int>("depth") == depth }
                        .map { it as TiledMapTileLayer }
                layers.forEach { layer ->
                    renderer.renderTileLayer(layer)
                }
            }
            entities.filter { mPosition[it].depth == depth }.forEach {
                val position = mPosition[it].position
                spriteBatch.draw(mTexture[it].texture, position.x, position.y)
            }
        }
    }

    fun calculateDepthRange(chunks: List<Chunk>, entities: IntArray): Pair<Int?, Int?>{

        var min: Int? = null
        var max: Int? = null
        for(chunk in chunks){
            val chunkMin = chunk.getMinDepth()
            if(min == null){
                min = chunkMin
            }else if(chunkMin != null && chunkMin < min){
                min = chunkMin
            }
            val chunkMax = chunk.getMinDepth()
            if(max == null){
                max = chunkMax
            }else if(chunkMax != null && chunkMax > max){
                max = chunkMax
            }
        }
        for(entity in entities){
            val depth = mPosition[entity].depth
            if(min == null){
                min = depth
            }else if(max == null){
                max = depth
            } else if(depth < min){
                min = depth
            }else if(depth > max){
                max = depth
            }
        }
        return  Pair(min, max)
    }

    fun warnDepth(chunks: List<Chunk>){
        chunks.forEach { chunk ->
            chunk.tileMap.layers.map { it as TiledMapTileLayer }.filter { it.propertyOrNull<Int>("depth") == null }.forEach {
                info { "Failed to render layer, missing depth property" }
            }
        }
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

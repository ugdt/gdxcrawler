package com.abysl.gdxcrawler.rendering

import com.abysl.gdxcrawler.data.Aspects
import com.abysl.gdxcrawler.ecs.components.BodyComponent
import com.abysl.gdxcrawler.ecs.components.PositionComponent
import com.abysl.gdxcrawler.ecs.components.TextureComponent
import com.abysl.gdxcrawler.physics.Body
import com.abysl.gdxcrawler.physics.CircleBody
import com.abysl.gdxcrawler.physics.RectBody
import com.abysl.gdxcrawler.settings.RenderSettings
import com.abysl.gdxcrawler.world.Chunk
import com.abysl.gdxcrawler.world.WorldMap
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.World
import com.artemis.managers.TagManager
import com.artemis.utils.IntBag
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import kotlin.math.roundToInt

class GameRenderer(val world: World, private val worldMap: WorldMap, private val renderSettings: RenderSettings) {
    private val spriteBatch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val camera: OrthographicCamera = world.getRegistered(OrthographicCamera::class.java)
        ?: OrthographicCamera(renderSettings.baseWidth, renderSettings.baseHeight)
    private val tagManager = world.getSystem(TagManager::class.java)
    private val positionMapper: ComponentMapper<PositionComponent> = world.getMapper(PositionComponent::class.java)
    private val textureMapper: ComponentMapper<TextureComponent> = world.getMapper(TextureComponent::class.java)
    private val bodyMapper: ComponentMapper<BodyComponent> = world.getMapper(BodyComponent::class.java)

    fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val player = tagManager.getEntity("PLAYER")
        val pos = player.getComponent(PositionComponent::class.java).position

        camera.position.set(pos.x, pos.y, 0f)
        camera.update()

        val drawables: List<Pair<Vector2, Drawable>> =
            (getEntities(Aspects.DRAWABLE.aspect).map(::entityToDrawable) + worldMap.getActiveChunks().flatMap(::chunkToDrawables))
                .sortedWith(compareBy({ it.second.depth }, { if (it.second is DrawableLayer) -1 else 1 }))
        drawables.forEach {
            spriteBatch.projectionMatrix = camera.combined
            it.second.draw(spriteBatch, it.first.x, it.first.y, 1f, 1f)
        }
        renderBodies(shapeRenderer)
    }

    private fun renderBodies(shapeRenderer: ShapeRenderer) {
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLACK

        getEntities(Aspects.BODY.aspect).forEach {
            val pos: Vector2 = positionMapper[it].position
            val body: Body = bodyMapper[it].body
            if (body is RectBody) {
                shapeRenderer.rect(pos.x, pos.y, body.length, body.width)
            }
            if (body is CircleBody) {
                shapeRenderer.circle(pos.x, pos.y, body.radius)
            }
        }
        shapeRenderer.end()
    }

    private fun entityToDrawable(id: Int): Pair<Vector2, Drawable> {
        val positionComponent = positionMapper[id]
        return positionComponent.position to DrawableTexture(textureMapper[id].texture, positionComponent.depth)
    }

    private fun chunkToDrawables(chunk: Chunk): List<Pair<Vector2, Drawable>> {
        val result: MutableList<Pair<Vector2, Drawable>> = mutableListOf()
        for (layer in chunk.tileMap.layers) {
            val drawable = DrawableLayer(camera, chunk.tileMap, layer as TiledMapTileLayer, renderSettings.tileSize)
            val size = chunk.size.toFloat()
            val position = Vector2(chunk.chunkPosition.x * size, chunk.chunkPosition.y * size)
            result.add(position to drawable)
        }
        return result
    }

    private fun getEntities(aspect: Aspect.Builder): List<Int> {
        val entities: IntBag = world.aspectSubscriptionManager.get(aspect).entities
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
        camera.viewportWidth = (renderSettings.baseWidth + deltaWidth)
        camera.viewportHeight = (renderSettings.baseHeight + deltaHeight)

        if (width % 2 != 0) {
            Gdx.graphics.setWindowedMode(width - 1, height)
        }

        if (height % 2 != 0) {
            Gdx.graphics.setWindowedMode(width, height - 1)
        }
    }

    fun dispose() {
        shapeRenderer.dispose()
        spriteBatch.dispose()
    }
}

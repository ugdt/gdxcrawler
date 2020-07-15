package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.ecs.components.CPhysics
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.components.CTexture
import com.abysl.gdxcrawler.ecs.getPlayerArchetype
import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.ecs.systems.SPhysics
import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.utils.PixelPerfectRenderer
import com.abysl.gdxcrawler.world.TileWorld
import com.abysl.gdxcrawler.world.level.DesertLevel
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Screen
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.math.*
import kotlin.math.roundToInt

class ECSWorldScreen : Screen, IPhysics {
    private val world: World
    private val playerId: Int
    private val playerEntity: Entity
    private val tagManager: TagManager
    private val pixelPerfectRenderer = PixelPerfectRenderer(320, 180)
    private val tileWorld = TileWorld(16, 16, DesertLevel())
    private val tileWorldRenderer = tileWorld.getRenderer(pixelPerfectRenderer.fboSpriteBatch)

    init {
        val config = WorldConfigurationBuilder()
                .with(
                        SEvent(),
                        SPhysics(),
                        TagManager()
                )
                .build()
        config.register("tilemap", tileWorld.tiledMap)
        world = World(config)

        tagManager = world.getSystem(TagManager::class.java)

        val playerArchetype = getPlayerArchetype(ArchetypeBuilder(), world)
        playerId = world.create(playerArchetype)
        playerEntity = initializePlayer(playerId, world, tagManager)

        tileWorldRenderer.setView(pixelPerfectRenderer.camera)
    }

    private fun initializePlayer(playerId: Int, world: World, tagManager: TagManager): Entity {
        tagManager.register("PLAYER", playerId)

        val playerEntity = world.getEntity(playerId)
        val cPhysics = playerEntity.getComponent(CPhysics::class.java)
        cPhysics.speed = 10
        cPhysics.friction = 0.05f

        return playerEntity
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        val playerTexture = playerEntity.getComponent(CTexture::class.java).texture
        val playerPosition = playerEntity.getComponent(CPosition::class.java).position

        val camera = pixelPerfectRenderer.camera
        val offset = Vector3(8f, 8f, 0f)
        camera.position.set(Vector3(playerPosition, 0f) + offset)
        tileWorldRenderer.setView(pixelPerfectRenderer.camera)

        pixelPerfectRenderer.render { spriteBatch ->
            tileWorldRenderer.render()
            spriteBatch.draw(playerTexture, playerPosition.x, playerPosition.y)
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        pixelPerfectRenderer.resize(width, height)
    }

    override fun dispose() {
    }

    override fun physics(delta: Float) {
        world.setDelta(delta)
        world.process()

        val playerPosition = playerEntity.getComponent(CPosition::class.java).position
        tileWorld.generateChunksAround(GridPoint2(playerPosition.x.toInt(), playerPosition.y.toInt()), 5)
    }
}
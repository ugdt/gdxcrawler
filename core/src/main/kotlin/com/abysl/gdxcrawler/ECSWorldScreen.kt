package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.ecs.components.CPhysics
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.components.CTexture
import com.abysl.gdxcrawler.ecs.getPlayerArchetype
import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.ecs.systems.SPhysics
import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.utils.PixelPerfectRenderer
import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Screen

class ECSWorldScreen : Screen, IPhysics {
    private val world: World
    private val playerId: Int
    private val tagManager: TagManager
    private val pixelPerfect = PixelPerfectRenderer(320, 180)

    init {
        val config = WorldConfigurationBuilder()
                .with(
                        SEvent(),
                        SPhysics(),
                        TagManager()
                ).build()
        world = World(config)

        tagManager = world.getSystem(TagManager::class.java)

        val playerArchetype = getPlayerArchetype(ArchetypeBuilder(), world)
        playerId = world.create(playerArchetype)
        initializePlayer(playerId, world, tagManager)
    }

    private fun initializePlayer(playerId: Int, world: World, tagManager: TagManager) {
        tagManager.register("PLAYER", playerId)

        val cPhysics = world.getEntity(playerId).getComponent(CPhysics::class.java)
        cPhysics.speed = 10
        cPhysics.friction = 0.05f
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        val playerEntity = world.getEntity(playerId)
        val playerTexture = playerEntity.getComponent(CTexture::class.java).texture
        val playerPosition = playerEntity.getComponent(CPosition::class.java).position

        pixelPerfect.render { spriteBatch ->
            spriteBatch.draw(playerTexture, playerPosition.x, playerPosition.y)
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        pixelPerfect.resize(width, height)
    }

    override fun dispose() {
    }

    override fun physics(delta: Float) {
        world.setDelta(delta)
        world.process()
    }
}
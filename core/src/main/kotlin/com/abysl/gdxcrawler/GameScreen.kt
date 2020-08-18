package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.ecs.components.CPhysics
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.getPlayerArchetype
import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.rendering.GameRenderer
import com.abysl.gdxcrawler.rendering.RenderSettings
import com.abysl.gdxcrawler.utils.GameWorld
import com.abysl.gdxcrawler.world.TileWorld
import com.abysl.gdxcrawler.world.level.TutorialLevel
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.Screen
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2

class GameScreen : Screen, IPhysics {
    private val tileWorld = TileWorld(64, TutorialLevel())
    private val world: World = World(GameWorld().build())
    private val playerId: Int
    private val playerEntity: Entity
    private val tagManager: TagManager = world.getSystem(TagManager::class.java)
    private val worldRenderer = GameRenderer(world, tileWorld, RenderSettings(16, 20f, 11.5f))

    init {
        val playerArchetype = getPlayerArchetype(ArchetypeBuilder(), world)
        playerId = world.create(playerArchetype)
        playerEntity = initializePlayer(playerId, world, tagManager)
    }

    private fun initializePlayer(playerId: Int, world: World, tagManager: TagManager): Entity {
        tagManager.register("PLAYER", playerId)

        val playerEntity = world.getEntity(playerId)
        val cPhysics = playerEntity.getComponent(CPhysics::class.java)
        val cPosition = playerEntity.getComponent(CPosition::class.java)
        cPosition.position = Vector2(0f, 0f)
        cPhysics.speed = 10
        return playerEntity
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        worldRenderer.render()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        worldRenderer.resize(width, height)
    }

    override fun dispose() {
    }

    override fun physics(delta: Float) {
        world.setDelta(delta)
        world.process()

        val playerPosition = playerEntity.getComponent(CPosition::class.java).position
        val gridPosition = GridPoint2(playerPosition.x.toInt(), playerPosition.y.toInt())
        tileWorld.setActiveChunks(tileWorld.getChunksAround(tileWorld.worldToChunk(gridPosition)))
    }
}
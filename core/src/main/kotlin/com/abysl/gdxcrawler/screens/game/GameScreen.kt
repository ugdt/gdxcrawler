package com.abysl.gdxcrawler.screens.game

import com.abysl.gdxcrawler.ecs.components.CMove
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.ecs.getPlayerArchetype
import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.rendering.GameRenderer
import com.abysl.gdxcrawler.settings.InputSettings
import com.abysl.gdxcrawler.settings.RenderSettings
import com.abysl.gdxcrawler.utils.GameWorld
import com.abysl.gdxcrawler.world.TileWorld
import com.abysl.gdxcrawler.world.level.TutorialLevel
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.Screen
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import ktx.preferences.get

class GameScreen : Screen, IPhysics {
    private val tileWorld = TileWorld(64, TutorialLevel())
    private val world: World = GameWorld().create()
    private val playerId: Int
    private val playerEntity: Entity
    private val tagManager: TagManager = world.getSystem(TagManager::class.java)
    private val worldRenderer = GameRenderer(world, tileWorld, RenderSettings(16, 20f, 11.5f))
    private val eventManager: EventManager = world.getRegistered("eventManager")
    private val prefs: Preferences = world.getRegistered("preferences")
    private val multiplexer: InputMultiplexer = InputMultiplexer()

    init {
        val playerArchetype = getPlayerArchetype(ArchetypeBuilder(), world)
        playerId = world.create(playerArchetype)
        playerEntity = initializePlayer(playerId, world, tagManager)
        multiplexer.addProcessor(GameInput(eventManager, prefs["inputSettings", InputSettings()]))
        Gdx.input.inputProcessor = multiplexer
    }

    private fun initializePlayer(playerId: Int, world: World, tagManager: TagManager): Entity {
        tagManager.register("PLAYER", playerId)

        val playerEntity = world.getEntity(playerId)
        val cPhysics = playerEntity.getComponent(CMove::class.java)
        val cPosition = playerEntity.getComponent(CPosition::class.java)
        cPosition.position = Vector2(0f, 0f)
        cPhysics.speed = 10f
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
        eventManager.process()
        world.process()

        val playerPosition = playerEntity.getComponent(CPosition::class.java).position
        val gridPosition = GridPoint2(playerPosition.x.toInt(), playerPosition.y.toInt())
        tileWorld.setActiveChunks(tileWorld.getChunksAround(tileWorld.worldToChunk(gridPosition)))
    }
}

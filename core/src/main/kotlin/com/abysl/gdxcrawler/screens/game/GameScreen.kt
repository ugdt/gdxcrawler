package com.abysl.gdxcrawler.screens.game

import com.abysl.gdxcrawler.ecs.components.BodyComponent
import com.abysl.gdxcrawler.ecs.components.MoveComponent
import com.abysl.gdxcrawler.ecs.components.PositionComponent
import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.ecs.getPlayerArchetype
import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.physics.RectBody
import com.abysl.gdxcrawler.rendering.GameRenderer
import com.abysl.gdxcrawler.settings.InputSettings
import com.abysl.gdxcrawler.settings.RenderSettings
import com.abysl.gdxcrawler.utils.GameWorld
import com.abysl.gdxcrawler.utils.GameWorldConstants
import com.abysl.gdxcrawler.world.WorldMap
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
    private val world: World = GameWorld().create()
    private val playerId: Int
    private val playerEntity: Entity
    private val tagManager: TagManager = world.getSystem(TagManager::class.java)
    private val worldMap: WorldMap = world.getRegistered(GameWorldConstants.WORLD_MAP)
    private val worldRenderer = GameRenderer(world, worldMap, RenderSettings(16, 20f, 11.5f))
    private val eventManager: EventManager = world.getRegistered(GameWorldConstants.EVENT_MANAGER)
    private val prefs: Preferences = world.getRegistered(GameWorldConstants.PREFERENCES)
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
        val moveComponent = playerEntity.getComponent(MoveComponent::class.java)
        val positionComponent = playerEntity.getComponent(PositionComponent::class.java)
        val bodyComponent = playerEntity.getComponent(BodyComponent::class.java)
        bodyComponent.body = RectBody(1f, 1f)
        positionComponent.position = Vector2(0f, 0f)
        moveComponent.speed = 10f
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

        val playerPosition = playerEntity.getComponent(PositionComponent::class.java).position
        val gridPosition = GridPoint2(playerPosition.x.toInt(), playerPosition.y.toInt())
        worldMap.setActiveChunks(worldMap.getChunksAround(worldMap.worldToChunk(gridPosition)))
    }
}

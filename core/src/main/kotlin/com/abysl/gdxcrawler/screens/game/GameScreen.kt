package com.abysl.gdxcrawler.screens.game

import com.abysl.gdxcrawler.ecs.components.CPhysics
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.getPlayerArchetype
import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.utils.GameRenderer
import com.abysl.gdxcrawler.utils.GameWorld
import com.abysl.gdxcrawler.world.TileWorld
import com.abysl.gdxcrawler.world.level.TutorialLevel
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2

class GameScreen : Screen, IPhysics {
    private val tileWorld = TileWorld(16, 16, TutorialLevel())
    private val world: World = World(GameWorld(tileWorld.tiledMap).build())
    private val playerId: Int
    private val playerEntity: Entity
    private val tagManager: TagManager = world.getSystem(TagManager::class.java)
    private val gameRenderer = GameRenderer(world, 20f, 11.25f, 16)
    private val hud = GameHud()
    private val multiplexer = InputMultiplexer()

    init {
        val playerArchetype = getPlayerArchetype(ArchetypeBuilder(), world)
        playerId = world.create(playerArchetype)
        playerEntity = initializePlayer(playerId, world, tagManager)
        multiplexer.addProcessor(hud)
        multiplexer.addProcessor(world.getSystem(SEvent::class.java))
        Gdx.input.inputProcessor = multiplexer
    }

    private fun initializePlayer(playerId: Int, world: World, tagManager: TagManager): Entity {
        tagManager.register("PLAYER", playerId)

        val playerEntity = world.getEntity(playerId)
        val cPhysics = playerEntity.getComponent(CPhysics::class.java)
        val cPosition = playerEntity.getComponent(CPosition::class.java)
        cPosition.position = Vector2(0f, 0f)
        cPhysics.speed = 1
        return playerEntity
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        gameRenderer.render()
        hud.draw()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        gameRenderer.update(width, height)
        hud.viewport.update(width, height)
    }

    override fun dispose() {
        gameRenderer.dispose()
        world.dispose()
        hud.dispose()
    }

    override fun physics(delta: Float) {
        world.setDelta(delta)
        world.process()

        val playerPosition = playerEntity.getComponent(CPosition::class.java).position
        tileWorld.generateChunksAround(GridPoint2(playerPosition.x.toInt(), playerPosition.y.toInt()), 5)
    }
}
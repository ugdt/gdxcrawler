package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.ecs.components.CPhysics
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.getPlayerArchetype
import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.ecs.systems.SPhysics
import com.abysl.gdxcrawler.physics.IPhysics
import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Screen

class ECSWorldScreen : Screen, IPhysics {
    private val world: World
    private val player: Int
    private val tagManager: TagManager
    private var timer = 0.0

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
        player = world.create(playerArchetype)
        tagManager.register("PLAYER", player)
        world.getEntity(player).getComponent(CPhysics::class.java).speed = 10
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }

    override fun physics(delta: Float) {
        timer += delta
        world.setDelta(delta)
        world.process()

        if (timer > 1) {
            println("${world.getEntity(player).getComponent(CPosition::class.java).position}")
            timer = 0.0
        }
    }
}
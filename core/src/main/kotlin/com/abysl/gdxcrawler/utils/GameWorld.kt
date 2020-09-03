package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.ecs.systems.MovementSystem
import com.abysl.gdxcrawler.world.WorldMap
import com.abysl.gdxcrawler.world.level.C25DemoLevel
import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx

class GameWorld : WorldConfigurationBuilder() {
    init {
        with(
            MovementSystem(),
            TagManager()
        )
    }

    fun create(): World {
        val worldConfig: WorldConfiguration = GameWorld().build()
        worldConfig.register(GameWorldConstants.WORLD_MAP, WorldMap(64, C25DemoLevel()))
        worldConfig.register(GameWorldConstants.EVENT_MANAGER, EventManager())
        worldConfig.register(GameWorldConstants.PREFERENCES, Gdx.app.getPreferences("com.abysl.gdxcrawler"))
        return World(worldConfig)
    }
}

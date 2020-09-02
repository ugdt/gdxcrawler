package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.ecs.systems.SMovement
import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx

class GameWorld : WorldConfigurationBuilder() {
    init {
        with(
            SEvent(),
            SMovement(),
            TagManager()
        )
    }
    fun create(): World {
        val worldConfig: WorldConfiguration = GameWorld().build()
        worldConfig.register("eventManager", EventManager())
        val preferences = Gdx.app.getPreferences("com.abysl.gdxcrawler")
        worldConfig.register("preferences", preferences)
        return World(worldConfig)
    }
}

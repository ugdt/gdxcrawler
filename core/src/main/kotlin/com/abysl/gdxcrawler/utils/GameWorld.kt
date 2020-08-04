package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.ecs.systems.SPhysics
import com.artemis.WorldConfiguration
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.maps.tiled.TiledMap

class GameWorld(val tileMap: TiledMap) : WorldConfigurationBuilder() {
    init {
        with(
                SEvent(),
                SPhysics(),
                TagManager()
        )
    }

    override fun build(): WorldConfiguration {
        val world = super.build()
        world.register("tileMap", tileMap)
        return world
    }
}
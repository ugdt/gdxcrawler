package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.ecs.systems.SPhysics
import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager

class GameWorld(tileMap: TiledMapTile) : World(worldConfig) {
    companion object {
        val worldConfig: WorldConfiguration

        init {
            val worldConfigBuilder = WorldConfigurationBuilder()
                .with(
                        SEvent(),
                        SPhysics(),
                        TagManager()
                )

            worldConfigBuilder.register()
            worldConfig = worldConfigBuilder.build()
        }
    }

    val tileMap = tileMap
}
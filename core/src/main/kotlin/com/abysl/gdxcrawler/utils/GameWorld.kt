package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.ecs.systems.SPhysics
import com.artemis.WorldConfiguration
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.maps.tiled.TiledMap

class GameWorld() : WorldConfigurationBuilder() {
    init {
        with(
                SEvent(),
                SPhysics(),
                TagManager()
        )
    }
}
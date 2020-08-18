package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.systems.SEvent
import com.abysl.gdxcrawler.ecs.systems.SPhysics
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager

class GameWorld : WorldConfigurationBuilder() {
    init {
        with(
                SEvent(),
                SPhysics(),
                TagManager()
        )
    }
}
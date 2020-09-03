package com.abysl.gdxcrawler.utils

import com.abysl.gdxcrawler.ecs.components.PositionComponent
import com.abysl.gdxcrawler.ecs.components.TextureComponent
import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.ecs.systems.MovementSystem
import com.abysl.gdxcrawler.world.WorldMap
import com.abysl.gdxcrawler.world.level.C25DemoLevel
import com.artemis.Entity
import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2

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
        val world: World = World(worldConfig)
        val entity: Entity = world.createEntity()
        entity.edit().add(TextureComponent()).add(PositionComponent(Vector2(-2f, -2f)))
        return world
    }
}

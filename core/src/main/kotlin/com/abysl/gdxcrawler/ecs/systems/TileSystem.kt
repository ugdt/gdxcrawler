package com.abysl.gdxcrawler.ecs.systems

import com.abysl.gdxcrawler.ecs.components.PositionComponent
import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.utils.GameWorldConstants
import com.abysl.gdxcrawler.world.WorldMap
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem

@All(PositionComponent::class)

class TileSystem : IteratingSystem() {
    @Wire(name = GameWorldConstants.EVENT_MANAGER)
    lateinit var eventManager: EventManager
    @Wire(name = GameWorldConstants.WORLD_MAP)
    lateinit var worldMap: WorldMap

    override fun process(entityId: Int) {
    }
}

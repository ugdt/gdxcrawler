package com.abysl.gdxcrawler.ecs.systems

import com.abysl.gdxcrawler.ecs.components.CEvents
import com.abysl.gdxcrawler.ecs.components.CMove
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.ecs.events.LocalPlayerMoveEvent
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.managers.TagManager
import com.artemis.systems.IteratingSystem
import ktx.math.plus
import ktx.math.times

@All(CMove::class, CPosition::class)
class SMovement : IteratingSystem() {
    private lateinit var mMove: ComponentMapper<CMove>
    private lateinit var mPosition: ComponentMapper<CPosition>
    private lateinit var mEvents: ComponentMapper<CEvents>
    private lateinit var tagManager: TagManager

    @Wire(name = "eventManager")
    lateinit var eventManager: EventManager

    override fun initialize() {
        tagManager = world.getSystem(TagManager::class.java)

        eventManager.subscribe<LocalPlayerMoveEvent> {
            mMove[tagManager.getEntityId("PLAYER")].direction += it.direction
        }
    }

    override fun process(entityId: Int) {
        val cMove: CMove = mMove[entityId]
        val cPosition: CPosition = mPosition[entityId]
        cPosition.position += (cMove.getVelocity() * world.delta)
    }
}

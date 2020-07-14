package com.abysl.gdxcrawler.ecs.systems

import com.abysl.gdxcrawler.ecs.components.CEvents
import com.abysl.gdxcrawler.ecs.components.CPhysics
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.input.MoveEvent
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.managers.TagManager
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.math.*

@All(CPhysics::class)
class SPhysics : IteratingSystem() {
    lateinit var mPhysics: ComponentMapper<CPhysics>
    lateinit var mPosition: ComponentMapper<CPosition>
    lateinit var mEvents: ComponentMapper<CEvents>
    lateinit var tagManager: TagManager

    private var ePlayer = -1
    private var eEvents = -1

    override fun initialize() {
        tagManager = world.getSystem(TagManager::class.java)

    }

    override fun process(entityId: Int) {
        if (ePlayer == -1) {
            ePlayer = tagManager.getEntityId("PLAYER")
        }

        if (eEvents == -1) {
            eEvents = tagManager.getEntityId("EVENTS")
        }

        if (mPosition.has(entityId)) {
            val cPhysics = mPhysics.create(entityId)
            val cPosition = mPosition.create(entityId)
            val delta = world.delta

            if (entityId == ePlayer) {
                if (eEvents != -1) {
                    val cEvents = mEvents.create(eEvents)

                    var moveVector2 = Vector2.Zero

                    for (event in cEvents.events) {
                        if (event is MoveEvent) {
                            moveVector2 = moveVector2 + event.direction
                        }
                    }

                    moveVector2 = moveVector2.nor() * cPhysics.speed * delta

                    cPhysics.velocity = cPhysics.velocity + (moveVector2)
                }
            }

            if (cPosition != null) {
                cPhysics.velocity = cPhysics.velocity + (cPhysics.acceleration * delta)
                cPosition.position = cPosition.position + (cPhysics.velocity * delta)
            }
        }
    }
}
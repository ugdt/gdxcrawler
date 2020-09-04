package com.abysl.gdxcrawler.ecs.systems

import com.abysl.gdxcrawler.ecs.components.BodyComponent
import com.abysl.gdxcrawler.ecs.components.MoveComponent
import com.abysl.gdxcrawler.ecs.components.PositionComponent
import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.ecs.events.LocalPlayerMoveEvent
import com.abysl.gdxcrawler.physics.RectBody
import com.abysl.gdxcrawler.utils.GameWorldConstants
import com.abysl.gdxcrawler.world.WorldMap
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.managers.TagManager
import com.artemis.systems.IteratingSystem
import ktx.math.plus
import ktx.math.times
import ktx.math.vec2

@All(MoveComponent::class, PositionComponent::class)
class MovementSystem : IteratingSystem() {
    private lateinit var moveMapper: ComponentMapper<MoveComponent>
    private lateinit var positionMapper: ComponentMapper<PositionComponent>
    private lateinit var bodyMapper: ComponentMapper<BodyComponent>
    private lateinit var tagManager: TagManager

    @Wire(name = GameWorldConstants.EVENT_MANAGER)
    lateinit var eventManager: EventManager
    @Wire(name = GameWorldConstants.WORLD_MAP)
    lateinit var worldMap: WorldMap

    override fun initialize() {
        tagManager = world.getSystem(TagManager::class.java)

        eventManager.subscribe<LocalPlayerMoveEvent> {
            moveMapper[tagManager.getEntityId("PLAYER")].direction += it.direction
        }
    }

    override fun process(entityId: Int) {
        val moveComponent: MoveComponent = moveMapper[entityId]
        val positionComponent: PositionComponent = positionMapper[entityId]
        val bodyComponent: BodyComponent = bodyMapper[entityId]

        val newPositionX = positionComponent.position.x + (moveComponent.getVelocity().x * world.delta)
        val newPositionY = positionComponent.position.y + (moveComponent.getVelocity().y * world.delta)

        val body = bodyComponent.body

        if (body is RectBody) {
            val collidesX = worldMap.collides(*body.getVertices(vec2(newPositionX, positionComponent.position.y)).toTypedArray()).isNotEmpty()
            val collidesY = worldMap.collides(*body.getVertices(vec2(positionComponent.position.x, newPositionY)).toTypedArray()).isNotEmpty()

            if (! collidesX) {
                positionComponent.position.x = newPositionX
            }

            if (! collidesY) {
                positionComponent.position.y = newPositionY
            }
        }
    }
}

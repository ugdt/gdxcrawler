package com.abysl.gdxcrawler.data

import com.abysl.gdxcrawler.ecs.components.*
import com.artemis.Aspect

enum class Aspects(val aspect: Aspect.Builder) {
    PLAYER(Aspect.all(LocalPlayerComponent::class.java)),
    BODY(Aspect.all(BodyComponent::class.java, PositionComponent::class.java).exclude(GhostComponent::class.java)),
    DRAWABLE(Aspect.all(TextureComponent::class.java, PositionComponent::class.java)),
    MOVABLE(Aspect.all(MoveComponent::class.java))
    ;

    companion object {
        operator fun get(name: String): Aspect.Builder {
            val match: Aspects = values().firstOrNull { it.name == name.toUpperCase() }
                ?: error("Aspect \"$name\" does not exist.")
            return match.aspect
        }
    }
}

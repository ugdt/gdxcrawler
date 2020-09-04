package com.abysl.gdxcrawler.ecs

import com.abysl.gdxcrawler.ecs.components.BodyComponent
import com.abysl.gdxcrawler.ecs.components.MoveComponent
import com.abysl.gdxcrawler.ecs.components.PositionComponent
import com.abysl.gdxcrawler.ecs.components.TextureComponent
import com.artemis.Archetype
import com.artemis.ArchetypeBuilder
import com.artemis.World

fun getPlayerArchetype(archetypeBuilder: ArchetypeBuilder, world: World): Archetype {
    return archetypeBuilder
        .add(MoveComponent::class.java)
        .add(PositionComponent::class.java)
        .add(TextureComponent::class.java)
        .add(BodyComponent::class.java)
        .build(world)
}

package com.abysl.gdxcrawler.ecs

import com.abysl.gdxcrawler.ecs.components.CPhysics
import com.abysl.gdxcrawler.ecs.components.CPosition
import com.abysl.gdxcrawler.ecs.components.CTexture
import com.artemis.Archetype
import com.artemis.ArchetypeBuilder
import com.artemis.World

fun getPlayerArchetype(archetypeBuilder: ArchetypeBuilder, world: World): Archetype {
    return archetypeBuilder
            .add(CPhysics::class.java)
            .add(CPosition::class.java)
            .add(CTexture::class.java)
            .build(world)
}
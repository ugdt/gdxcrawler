package com.abysl.gdxcrawler.ecs.components

import com.artemis.Component
import com.badlogic.gdx.math.Vector2

class PositionComponent(
    var position: Vector2 = Vector2.Zero,
    var depth: Int = 0
) : Component()

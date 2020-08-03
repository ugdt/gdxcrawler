package com.abysl.gdxcrawler.ecs.components

import com.artemis.Component
import com.badlogic.gdx.math.Vector2

class CPhysics : Component() {
    var speed = 0
    var velocity: Vector2 = Vector2.Zero
}
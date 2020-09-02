package com.abysl.gdxcrawler.ecs.components

import com.artemis.Component
import com.badlogic.gdx.math.Vector2

class CMove(var speed: Float = 0f, var direction: Vector2 = Vector2.Zero) : Component() {

    /**
     * @return a new vector containing the current velocity
     */
    fun getVelocity(): Vector2 {
        // Make sure to copy the direction vector so we don't modify it when calling this function
        val velocity = direction.cpy()
        velocity.setLength(speed)
        return velocity
    }
}

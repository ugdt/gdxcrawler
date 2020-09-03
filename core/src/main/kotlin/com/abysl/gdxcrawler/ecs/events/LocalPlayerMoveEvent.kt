package com.abysl.gdxcrawler.ecs.events

import com.badlogic.gdx.math.Vector2

data class LocalPlayerMoveEvent(val direction: Vector2) : Event() {
    constructor(x: Float, y: Float) : this(Vector2(x, y))
}

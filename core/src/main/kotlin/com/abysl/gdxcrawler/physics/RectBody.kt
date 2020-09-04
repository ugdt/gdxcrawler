package com.abysl.gdxcrawler.physics

import com.badlogic.gdx.math.Vector2

class RectBody(val length: Float, val width: Float) : Body() {
    override fun collides(otherBody: Body): Boolean {
        return false // TODO: implement
    }

    fun getVertices(origin: Vector2): List<Vector2> {
        val vertices = mutableListOf<Vector2>()

        vertices.add(origin)
        vertices.add(Vector2(origin.x + length, origin.y))
        vertices.add(Vector2(origin.x, origin.y + width))
        vertices.add(Vector2(origin.x + length, origin.y + length))

        return vertices
    }
}

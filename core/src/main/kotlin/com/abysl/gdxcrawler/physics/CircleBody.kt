package com.abysl.gdxcrawler.physics

class CircleBody(
    val radius: Float
) : Body() {
    override fun collides(otherBody: Body): Boolean {
        return false // TODO: implement
    }
}

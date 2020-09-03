package com.abysl.gdxcrawler.physics

abstract class Body {
    abstract fun collides(otherBody: Body): Boolean
}

package com.abysl.gdxcrawler.utils

import com.badlogic.gdx.math.GridPoint2

fun gp2(x: Int = 0, y: Int = 0): GridPoint2 = GridPoint2(x, y)

operator fun GridPoint2.unaryMinus(): GridPoint2 = GridPoint2(-x, -y)

operator fun GridPoint2.plusAssign(gridPoint2: GridPoint2) {
    add(gridPoint2)
}

operator fun GridPoint2.plusAssign(addend: Int) {
    add(addend, addend)
}

operator fun GridPoint2.minusAssign(gridPoint2: GridPoint2) {
    sub(gridPoint2)
}

operator fun GridPoint2.minusAssign(subtraend: Int) {
    sub(subtraend, subtraend)
}

operator fun GridPoint2.timesAssign(gridPoint2: GridPoint2) {
    x *= gridPoint2.x
    y *= gridPoint2.y
}

operator fun GridPoint2.timesAssign(scalar: Int) {
    x *= scalar
    y *= scalar
}

operator fun GridPoint2.divAssign(gridPoint2: GridPoint2) {
    x /= gridPoint2.x
    y /= gridPoint2.y
}

operator fun GridPoint2.divAssign(scalar: Int) {
    x /= scalar
    y /= scalar
}

operator fun GridPoint2.plus(gridPoint2: GridPoint2): GridPoint2 = GridPoint2(x + gridPoint2.x, y + gridPoint2.y)

operator fun GridPoint2.plus(addend: Int): GridPoint2 = GridPoint2(x + addend, y + addend)

operator fun GridPoint2.minus(gridPoint2: GridPoint2): GridPoint2 = GridPoint2(x + gridPoint2.x, y + gridPoint2.y)

operator fun GridPoint2.minus(subtraend: Int): GridPoint2 = GridPoint2(x - subtraend, y - subtraend)

operator fun GridPoint2.times(gridPoint2: GridPoint2): GridPoint2 = GridPoint2(x * gridPoint2.x, y * gridPoint2.y)

operator fun GridPoint2.times(scalar: Int): GridPoint2 = GridPoint2(x * scalar, y * scalar)

operator fun GridPoint2.div(gridPoint2: GridPoint2): GridPoint2 = GridPoint2(x / gridPoint2.x, y / gridPoint2.y)

operator fun GridPoint2.div(scalar: Int): GridPoint2 = GridPoint2(x / scalar, y / scalar)

operator fun GridPoint2.inc(): GridPoint2 = GridPoint2(x + 1, y + 1)

operator fun GridPoint2.dec(): GridPoint2 = GridPoint2(x - 1, y - 1)

operator fun GridPoint2.component1(): Int = x

operator fun GridPoint2.component2(): Int = y

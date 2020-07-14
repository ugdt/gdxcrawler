package com.abysl.gdxcrawler.input

import com.badlogic.gdx.Input

enum class MoveEventEnum(val keyId: Int) {
    UP(Input.Keys.W),
    DOWN(Input.Keys.S),
    LEFT(Input.Keys.A),
    RIGHT(Input.Keys.D)
}
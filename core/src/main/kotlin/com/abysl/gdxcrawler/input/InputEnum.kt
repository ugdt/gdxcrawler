package com.abysl.gdxcrawler.input

import com.badlogic.gdx.Input

enum class InputEnum(val keyId: Int) {
    UP(Input.Keys.W),
    DOWN(Input.Keys.S),
    LEFT(Input.Keys.A),
    RIGHT(Input.Keys.D),
    SPACE(Input.Keys.SPACE)
}
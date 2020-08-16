package com.abysl.gdxcrawler.ecs.components

import com.artemis.Component
import com.badlogic.gdx.graphics.Texture

class CTexture : Component() {
    var texture: Texture = Texture("default.png")
    var width: Float = 1f
    var height: Float = 1f
}
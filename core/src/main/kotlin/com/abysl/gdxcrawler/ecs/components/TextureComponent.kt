package com.abysl.gdxcrawler.ecs.components

import com.artemis.Component
import com.badlogic.gdx.graphics.Texture

class TextureComponent : Component() {
    var texture: Texture = Texture("default.png")
}

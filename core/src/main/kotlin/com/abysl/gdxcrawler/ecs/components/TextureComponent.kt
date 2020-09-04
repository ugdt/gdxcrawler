package com.abysl.gdxcrawler.ecs.components

import com.artemis.Component
import com.badlogic.gdx.graphics.Texture

class TextureComponent(
    var texture: Texture = Texture("default.png")
) : Component()

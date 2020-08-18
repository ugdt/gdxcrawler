package com.abysl.gdxcrawler.ecs.components

import com.abysl.gdxcrawler.rendering.Drawable
import com.artemis.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.graphics.use

class CTexture : Component(){
    var texture: Texture = Texture("default.png")
}
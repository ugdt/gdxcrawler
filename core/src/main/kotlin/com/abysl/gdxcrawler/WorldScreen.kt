package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.utils.PixelPerfectScreen
import com.abysl.gdxcrawler.world.World
import com.badlogic.gdx.math.GridPoint2

class WorldScreen : PixelPerfectScreen(320, 180) {
    private val world = World(16, 16)
    private val worldMapRenderer = world.getRenderer(spriteBatch)

    init {
        world.generateChunksAround(GridPoint2(0, 0), 5)
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun draw(delta: Float) {
        worldMapRenderer.render()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }
}
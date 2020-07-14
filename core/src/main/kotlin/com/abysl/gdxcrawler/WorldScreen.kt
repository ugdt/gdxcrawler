package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.utils.PixelPerfectScreen
import com.abysl.gdxcrawler.world.World
import com.abysl.gdxcrawler.world.level.DesertLevel
import com.badlogic.gdx.math.GridPoint2

class WorldScreen : IPhysics, PixelPerfectScreen(320, 180) {
    private val world = World(16, 16, DesertLevel())
    private val worldMapRenderer = world.getRenderer(fboSpriteBatch)

    init {
        world.generateChunksAround(GridPoint2(0, 0), 5)
        worldMapRenderer.setView(camera)
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun draw(delta: Float) {
        worldMapRenderer.setView(camera)
        worldMapRenderer.render()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

    override fun physics(delta: Float) {
    }
}
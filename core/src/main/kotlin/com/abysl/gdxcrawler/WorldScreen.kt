package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.utils.PixelPerfectRenderer
import com.abysl.gdxcrawler.world.World
import com.abysl.gdxcrawler.world.level.DesertLevel
import com.badlogic.gdx.Screen
import com.badlogic.gdx.math.GridPoint2

class WorldScreen : Screen {
    private val world = World(16, 16, DesertLevel())
    private val pixelPerfectRenderer = PixelPerfectRenderer(320, 180)
    private val worldMapRenderer = world.getRenderer(pixelPerfectRenderer.fboSpriteBatch)

    init {
        world.generateChunksAround(GridPoint2(0, 0), 5)
        worldMapRenderer.setView(pixelPerfectRenderer.camera)
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        worldMapRenderer.setView(pixelPerfectRenderer.camera)

        pixelPerfectRenderer.render {
            worldMapRenderer.render()
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        pixelPerfectRenderer.resize(width, height)
    }

    override fun dispose() {
    }
}
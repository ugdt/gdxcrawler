package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.input.IInput
import com.abysl.gdxcrawler.input.InputEnum
import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.utils.PixelPerfectScreen
import com.abysl.gdxcrawler.world.World
import com.abysl.gdxcrawler.world.level.DesertLevel
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector3
import ktx.math.times

class WorldScreen : IInput, IPhysics, PixelPerfectScreen(320, 180) {
    private val world = World(16, 16, DesertLevel())
    private val worldMapRenderer = world.getRenderer(spriteBatch)
    private val cameraSpeed = 10
    private val cameraVelocity = Vector3.Zero

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

    override fun input(input: InputEnum) {
        when(input) {
            InputEnum.UP -> {
                cameraVelocity.y += cameraSpeed.toFloat()
            }
            InputEnum.DOWN -> {
                cameraVelocity.y -= cameraSpeed.toFloat()
            }
            InputEnum.RIGHT -> {
                cameraVelocity.x += cameraSpeed.toFloat()
            }
            InputEnum.LEFT -> {
                cameraVelocity.x -= cameraSpeed.toFloat()
            }
            else -> {
                // do nothing
            }
        }
    }

    override fun physics(delta: Float) {
        camera.translate(cameraVelocity * delta)
        cameraVelocity.x = 0f
        cameraVelocity.y = 0f
    }
}
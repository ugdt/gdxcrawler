package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.physics.IPhysics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import ktx.app.KtxGame
import ktx.async.KtxAsync

class Game : KtxGame<Screen>() {
    private val physicsTickRate = (1f / 144f) // 100 times per second
    private var accumulator = 0f

    override fun create() {
        KtxAsync.initiate()
        addScreen(MainMenu())
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }

    override fun render() {
        super.render()
        accumulator += Gdx.graphics.rawDeltaTime

        while (accumulator >= physicsTickRate) {
            physics(physicsTickRate)
            accumulator -= physicsTickRate
        }
    }

    private fun physics(delta: Float) {
        if (currentScreen is IPhysics) {
            val iPhysics = currentScreen as IPhysics
            iPhysics.physics(delta)
        }
    }
}

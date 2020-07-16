package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.physics.IPhysics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage

class Game : KtxGame<Screen>() {
    private val physicsTickRate = (1f / 100f) // 100 times per second
    private var accumulator = 0f

    val assetManager = AssetStorage()

    override fun create() {
        addScreen(MainMenu())
        addScreen(TestScreen())
        addScreen(ECSWorldScreen())
        setScreen<ECSWorldScreen>()
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
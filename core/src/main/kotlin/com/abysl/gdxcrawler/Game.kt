package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.input.IInput
import com.abysl.gdxcrawler.physics.IPhysics
import com.abysl.gdxcrawler.input.InputEnum
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.newAsyncContext
import kotlin.system.measureTimeMillis

class Game : KtxGame<Screen>() {
    private val physicsScope = CoroutineScope(newAsyncContext(2))
    private val physicsTickRate = (1f / 60f) // 60 times per second
    private var timeSinceLastTick = 0f

    val assetManager = AssetStorage()

    override fun create() {
        addScreen(MainMenu())
        addScreen(TestScreen())
        addScreen(WorldScreen())
        setScreen<WorldScreen>()

        physicsScope.launch {
            physicsLoop()
        }
    }

    override fun render() {
        super.render()

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            checkInput()
        }
    }

    private fun checkInput() {
        val currentScreens = screens.values().toList()

        for (key in InputEnum.values()) {
            if (Gdx.input.isKeyPressed(key.keyId)) {
                for (screen: Screen in currentScreens) {
                    if (screen is IInput) {
                        screen.input(key)
                    }
                }
            }
        }
    }

    private suspend fun physicsLoop() {
        while (true) {
            val currentScreens = screens.values().toList()

            val timeDiff = measureTimeMillis {
                delay((physicsTickRate * 1000).toLong())
            }

            timeSinceLastTick = timeDiff / 1000f

            for (screen: Screen in currentScreens) {
                if (screen is IPhysics) {
                    screen.physics(timeSinceLastTick)
                }
            }
        }
    }
}
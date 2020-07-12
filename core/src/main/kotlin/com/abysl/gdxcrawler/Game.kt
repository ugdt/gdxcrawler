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

    private lateinit var physicsScreens: List<IPhysics>
    private lateinit var inputScreens: List<IInput>

    val assetManager = AssetStorage()

    override fun create() {
        addScreen(MainMenu())
        addScreen(TestScreen())
        addScreen(WorldScreen())
        setScreen<WorldScreen>()

        val tempPhysicsScreens = mutableListOf<IPhysics>()
        val tempInputScreens = mutableListOf<IInput>()

        for (screen in screens.values()) {
            if (screen is IPhysics) {
                tempPhysicsScreens.add(screen)
            }
            if (screen is IInput) {
                tempInputScreens.add(screen)
            }
        }

        physicsScreens = tempPhysicsScreens.toList()
        inputScreens = tempInputScreens.toList()

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
        for (key in InputEnum.values()) {
            if (Gdx.input.isKeyPressed(key.keyId)) {
                for (inputScreen: IInput in inputScreens) {
                    inputScreen.input(key)
                }
            }
        }
    }

    private suspend fun physicsLoop() {
        while (true) {
            val timeDiff = measureTimeMillis {
                delay((physicsTickRate * 1000).toLong())
            }

            timeSinceLastTick = timeDiff / 1000f

            for (physicsScreen: IPhysics in physicsScreens) {
                physicsScreen.physics(timeSinceLastTick)
            }
        }
    }
}
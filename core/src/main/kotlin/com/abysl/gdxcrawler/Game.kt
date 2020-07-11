package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.utils.IPhysics
import com.badlogic.gdx.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import kotlin.system.measureTimeMillis

class Game : KtxGame<Screen>() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val physicsTickRate = (1f / 60f) // 60 times per second
    private var timeSinceLastTick = 0f

    val assetManager = AssetStorage()

    override fun create() {
        addScreen(MainMenu())
        addScreen(TestScreen())
        addScreen(WorldScreen())
        setScreen<TestScreen>()

        coroutineScope.launch {
            physicsLoop()
        }
    }

    private suspend fun physicsLoop() {
        while (true) {
            val timeDiff = measureTimeMillis {
                delay((physicsTickRate * 1000).toLong())
            }

            timeSinceLastTick = timeDiff / 1000f

            for (screen: Screen in screens.values()) {
                if (screen is IPhysics) {
                    screen.physics(timeSinceLastTick)
                }
            }
        }
    }
}
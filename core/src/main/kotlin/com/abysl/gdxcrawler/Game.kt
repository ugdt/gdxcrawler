package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.physics.IPhysics
import com.badlogic.gdx.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.newAsyncContext
import kotlin.system.measureTimeMillis

class Game : KtxGame<Screen>() {
    private val physicsScope = CoroutineScope(newAsyncContext(1))
    private val physicsTickRate = (1f / 120f) // 120 times per second
    private var timeSinceLastTick = 0f

    private lateinit var physicsScreens: List<IPhysics>

    val assetManager = AssetStorage()

    override fun create() {
        addScreen(MainMenu())
        addScreen(TestScreen())
        addScreen(WorldScreen())
        addScreen(ECSWorldScreen())
        setScreen<ECSWorldScreen>()

        val tempPhysicsScreens = mutableListOf<IPhysics>()

        for (screen in screens.values()) {
            if (screen is IPhysics) {
                tempPhysicsScreens.add(screen)
            }
        }

        physicsScreens = tempPhysicsScreens.toList()

        physicsScope.launch {
            physicsLoop()
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
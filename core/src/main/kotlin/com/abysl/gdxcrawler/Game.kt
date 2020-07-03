package com.abysl.gdxcrawler

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage

class Game : KtxGame<Screen>(){

    val assetManager = AssetStorage()

    override fun create() {
        // Registering ExampleScreen in the game object: it will be
        // accessible through ExampleScreen class:
        addScreen(MainMenu())
        addScreen(LoadingScreen())
        // Changing current screen to the registered instance of the
        // ExampleScreen class:
        setScreen<LoadingScreen>()
    }
}
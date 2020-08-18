package com.abysl.gdxcrawler.desktop

import com.abysl.gdxcrawler.Game
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    Lwjgl3Launcher.createApplication()
}

/** Launches the desktop (LWJGL3) application.  */
object Lwjgl3Launcher {
    internal fun createApplication(): Lwjgl3Application {
        return Lwjgl3Application(Game(), defaultConfiguration)
    }

    private val defaultConfiguration: Lwjgl3ApplicationConfiguration
        get() {
            val configuration = Lwjgl3ApplicationConfiguration()
            configuration.setTitle("gdxcrawler")
            configuration.setWindowedMode(1280, 720)
            configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
            configuration.useVsync(false)
            return configuration
        }
}
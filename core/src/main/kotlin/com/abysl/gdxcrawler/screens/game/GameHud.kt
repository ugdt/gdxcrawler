package com.abysl.gdxcrawler.screens.game

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors
import ktx.scene2d.progressBar
import ktx.scene2d.scene2d

class GameHud: Stage(ScreenViewport()) {
    init {
        VisUI.load()
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        this.actors {
            healthBar()
        }
    }

    fun healthBar(): ProgressBar {
        return scene2d.progressBar(0f, 100f, 1f)
    }

    override fun dispose() {
        super.dispose()
        VisUI.dispose()
    }

}
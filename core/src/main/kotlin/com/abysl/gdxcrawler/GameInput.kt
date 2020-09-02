package com.abysl.gdxcrawler

import com.abysl.gdxcrawler.ecs.events.EventManager
import com.abysl.gdxcrawler.settings.InputSettings
import com.badlogic.gdx.InputProcessor

class GameInput(val eventManager: EventManager, var settings: InputSettings) : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        settings.keyDownMap[keycode]?.let { event ->
            eventManager.publish(event)
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        settings.keyUpMap[keycode]?.let { event ->
            eventManager.publish(event)
            return true
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun scrolled(amount: Int): Boolean {
        TODO("Not yet implemented")
    }
}

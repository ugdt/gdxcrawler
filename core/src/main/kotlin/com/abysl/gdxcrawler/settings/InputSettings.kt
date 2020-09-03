package com.abysl.gdxcrawler.settings

import com.abysl.gdxcrawler.ecs.events.Event
import com.abysl.gdxcrawler.ecs.events.LocalPlayerMoveEvent
import com.badlogic.gdx.Input

data class InputSettings(
    val name: String = "inputSettings",
    val keyDownMap: Map<Int, Event> = mapOf(
        Input.Keys.W to LocalPlayerMoveEvent(0f, 1f),
        Input.Keys.A to LocalPlayerMoveEvent(-1f, 0f),
        Input.Keys.S to LocalPlayerMoveEvent(0f, -1f),
        Input.Keys.D to LocalPlayerMoveEvent(1f, 0f),
    ),
    // inverse directions of the keyDownMap to undo direction changes when the key is let go
    val keyUpMap: Map<Int, Event> = mapOf(
        Input.Keys.W to LocalPlayerMoveEvent(0f, -1f),
        Input.Keys.A to LocalPlayerMoveEvent(1f, 0f),
        Input.Keys.S to LocalPlayerMoveEvent(0f, 1f),
        Input.Keys.D to LocalPlayerMoveEvent(-1f, 0f),
    ),
)

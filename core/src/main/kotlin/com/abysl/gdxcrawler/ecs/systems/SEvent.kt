package com.abysl.gdxcrawler.ecs.systems

import com.abysl.gdxcrawler.ecs.components.CEvents
import com.abysl.gdxcrawler.input.Event
import com.abysl.gdxcrawler.input.MoveEvent
import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.IntMap
import com.badlogic.gdx.utils.IntSet
import ktx.collections.*

class SEvent : BaseSystem(), InputProcessor {
    private val events: GdxArray<Event>
    private val keyToEvent = IntMap<MoveEvent>()
    private var entity = -1
    private lateinit var mEvent: ComponentMapper<CEvents>
    private lateinit var cEvents: CEvents
    private val heldKeys: IntSet

    init {
        keyToEvent[Input.Keys.W] = MoveEvent(Vector2(0f, 1f))
        keyToEvent[Input.Keys.A] = MoveEvent(Vector2(-1f, 0f))
        keyToEvent[Input.Keys.S] = MoveEvent(Vector2(0f, -1f))
        keyToEvent[Input.Keys.D] = MoveEvent(Vector2(1f, 0f))

        heldKeys = IntSet(keyToEvent.size)
        events = GdxArray(keyToEvent.size)
    }

    override fun initialize() {
        entity = world.create()
        cEvents = mEvent.create(entity)

        val tagManager = world.getSystem(TagManager::class.java)
        tagManager.register("EVENTS", entity)
    }

    override fun begin() {
        for (key in keyToEvent.entries()) {
            val keyId = key.key

            if (heldKeys.contains(keyId)) {
                events.add(keyToEvent[keyId])
            }
        }
    }

    override fun processSystem() {
        cEvents.events = events.toList()
    }

    override fun end() {
        events.clear()
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (heldKeys.contains(keycode)) {
            heldKeys.remove(keycode)
            return true
        }
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keyToEvent.containsKey(keycode)) {
            heldKeys.add(keycode)
            return true
        }
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }
}

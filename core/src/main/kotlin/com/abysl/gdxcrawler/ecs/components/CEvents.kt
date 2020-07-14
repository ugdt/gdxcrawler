package com.abysl.gdxcrawler.ecs.components

import com.abysl.gdxcrawler.input.Event
import com.artemis.Component

class CEvents : Component() {
    lateinit var events: List<Event>
}
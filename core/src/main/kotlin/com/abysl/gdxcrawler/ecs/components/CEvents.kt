package com.abysl.gdxcrawler.ecs.components

import com.abysl.gdxcrawler.ecs.events.Event
import com.artemis.Component

class CEvents : Component() {
    lateinit var events: List<Event>
}

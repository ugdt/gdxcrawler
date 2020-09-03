package com.abysl.gdxcrawler.ecs.events

import java.util.*

class EventManager {

    // Have to do a MutableList<Any>, then do unsafe cast later. Couldn't think of another way to do it
    @PublishedApi
    internal val subscriptions: MutableMap<Class<out Event>, MutableList<Any>> = mutableMapOf()
    private val events: Queue<Event> = LinkedList()

    // don't really understand inline and reified, intellij just told me I need it, otherwise error
    inline fun <reified T : Event> subscribe(noinline subscriber: (T) -> Unit) {
        val event = T::class.java
        subscriptions.getOrPut(event) { mutableListOf() }.add(subscriber)
    }

    fun publish(event: Event) {
        events.add(event)
    }

    private fun <T : Event> processEvent(event: T) {
        subscriptions[event.javaClass]?.forEach {
            @Suppress("UNCHECKED_CAST")
            (it as (T) -> Unit).invoke(event)
        }
    }

    // Clear out the event queue
    fun process() {
        while (events.size > 0) {
            processEvent(events.poll())
        }
    }
}

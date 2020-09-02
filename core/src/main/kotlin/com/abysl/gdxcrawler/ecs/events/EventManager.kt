package com.abysl.gdxcrawler.ecs.events

import java.util.*

class EventManager {

    // has to be public because of inline fun, would rather be private
    // Have to do a MutableList<Any>, then do unsafe cast later. Couldn't think of another way to do it
    val subscriptions: MutableMap<Class<out Event>, MutableList<Any>> = mutableMapOf()
    private val events: Queue<Event> = LinkedList()

    // don't really understand inline and reified, intellij just told me I need it, otherwise error
    inline fun <reified T : Event> subscribe(noinline subscriber: (T) -> Unit) {
        val event = T::class.java
        if (subscriptions[event] == null) {
            subscriptions[event] = mutableListOf()
        }
        subscriptions[event]!!.add(subscriber)
    }

    fun publish(event: Event) {
        events.add(event)
    }

    private fun <T : Event> processEvent(event: T) {
        subscriptions[event.javaClass]?.forEach {
            (it as ((T) -> Unit)).invoke(event)
        }
    }

    // Clear out the event queue
    fun process() {
        while (events.size > 0) {
            processEvent(events.poll())
        }
    }
}

package de.hglabor.plugins.duels.events.events.duel

import de.hglabor.plugins.duels.duel.Duel
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class DuelStartEvent(val duel: Duel) : Event(true) {

    companion object {
        @JvmStatic
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}
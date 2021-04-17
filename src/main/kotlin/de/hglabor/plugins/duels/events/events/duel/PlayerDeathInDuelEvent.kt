package de.hglabor.plugins.duels.events.events.duel

import de.hglabor.plugins.duels.duel.AbstractDuel
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityDamageEvent

enum class DuelDeathReason {
    WATER, QUIT
}

class PlayerDeathInDuelEvent(
    val player: Player,
    val duel: AbstractDuel,
    val duelDeathReason: DuelDeathReason? = null,
    val bukkitDeathReason: EntityDamageEvent.DamageCause? = null
) : Event(false) {

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
package de.hglabor.plugins.duels.kits.specials.special

import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.specials.Specials
import de.hglabor.plugins.duels.player.DuelsPlayer
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerToggleSneakEvent
import kotlin.math.cos
import kotlin.math.sin

object NinjaSpecial {
    init {
        listen<PlayerToggleSneakEvent> {
            val player: Player = it.player
            val duelsPlayer = DuelsPlayer.get(player)
            val duel = duelsPlayer.currentDuel() ?: return@listen

            if (duel.kit.specials.contains(Specials.NINJA)) {
                val target = (duel.stats[player]?.get("lastHit") ?: return@listen) as Player
                if (duel.players.contains(target)) {
                    if (!Kits.hasCooldown(player)) {
                        Kits.setCooldown(player, 13)
                        var nang: Float = target.location.yaw + 90
                        if (nang < 0) nang += 360f
                        val nX = cos(Math.toRadians(nang.toDouble()))
                        val nZ = sin(Math.toRadians(nang.toDouble()))
                        val loc = target.location.clone().subtract(nX, 0.0, nZ)
                        player.teleport(loc)
                    }
                }
            }
        }
    }
}
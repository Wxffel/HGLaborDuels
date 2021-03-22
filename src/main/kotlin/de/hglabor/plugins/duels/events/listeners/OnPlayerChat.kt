package de.hglabor.plugins.duels.events.listeners

import de.hglabor.plugins.staff.utils.StaffData.isStaff
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlineSenders
import net.axay.kspigot.runnables.async
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.player.AsyncPlayerChatEvent

object OnPlayerChat {
    init {
        listen<AsyncPlayerChatEvent> {
            if (it.isCancelled) return@listen
            it.isCancelled = true

            val player = it.player
            var finalMessage = it.message

            if (player.isStaff)
                finalMessage = finalMessage.replace("&", "§")

            val recievers = arrayListOf<CommandSender>(Bukkit.getConsoleSender())

            async {
                onlineSenders.forEach { sender ->
                    sender.sendMessage("${KColors.GRAY}${player.displayName} ${KColors.DARKGRAY}» ${KColors.WHITE}$finalMessage")
                }
            }
        }
    }
}
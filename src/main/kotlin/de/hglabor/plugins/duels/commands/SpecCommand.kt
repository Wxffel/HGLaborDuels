package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.localization
import de.hglabor.plugins.duels.utils.PlayerFunctions.spec
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SpecCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (!player.isInFight() && !player.isInSoupsimulator()) {
                if (args.size == 1) {
                    val t = Bukkit.getPlayer(args[0])
                    if (t != null) {
                        if (t.isInFight()) {
                            player.spec(Data.duelIDFromPlayer[t]!!, true)
                        } else {
                            if (player.localization("de"))
                                player.sendMessage(
                                    Localization.SPEC_COMMAND_PLAYER_NOT_FIGHTING_DE.replace(
                                        "%playerName%", t.displayName))
                            else
                                player.sendMessage(
                                    Localization.SPEC_COMMAND_PLAYER_NOT_FIGHTING_EN.replace(
                                        "%playerName%", t.displayName))
                        }
                    } else {
                        if (player.localization("de"))
                            player.sendMessage(Localization.PLAYER_NOT_ONLINE_DE.replace("%playerName%", args[0]))
                        else
                            player.sendMessage(Localization.PLAYER_NOT_ONLINE_EN.replace("%playerName%", args[0]))
                    }
                } else {
                    if (player.localization("de"))
                        player.sendMessage(Localization.SPEC_COMMAND_HELP_DE)
                    else
                        player.sendMessage(Localization.SPEC_COMMAND_HELP_EN)
                }
            } else {
                if (player.localization("de"))
                    player.sendMessage(Localization.CANT_DO_THAT_RIGHT_NOW_DE)
                else
                    player.sendMessage(Localization.CANT_DO_THAT_RIGHT_NOW_EN)
            }
        }
        return false
    }

}
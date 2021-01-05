package de.hglabor.plugins.duels.commands

import de.hglabor.plugins.duels.duel.Duel
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.soupsimulator.isInSoupsimulator
import de.hglabor.plugins.duels.utils.Data
import de.hglabor.plugins.duels.utils.PlayerFunctions.isInFight
import de.hglabor.plugins.duels.utils.PlayerFunctions.sendLocalizedMessage
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.gui.openGUI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object ChallengeCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (!player.isInFight() && !player.isInSoupsimulator()) {
                // DUEL
                if (args.size == 1) {
                    val target = Bukkit.getPlayer(args[0])
                    if (target != null) {
                        Data.openedDuelGUI[player] = target
                        player.openGUI(ChooseKitGUI.gui)
                    } else {
                        player.sendLocalizedMessage(Localization.PLAYER_NOT_ONLINE_DE.replace("%playerName%", args[0]),
                            Localization.PLAYER_NOT_ONLINE_EN.replace("%playerName%", args[0]))
                    }

                    // ACCEPT
                } else if (args.size == 2 && args[0].equals("accept", true)) {
                    val target = Bukkit.getPlayer(args[1])
                    if (target != null) {
                        if (target.isInFight()) {
                            player.sendLocalizedMessage(Localization.CHALLENGE_COMMAND_ACCEPT_PLAYER_IN_FIGHT_DE.replace("%playerName%", target.name),
                                Localization.CHALLENGE_COMMAND_ACCEPT_PLAYER_IN_FIGHT_EN.replace("%playerName%", target.name),)
                            return false
                        }
                        if (Data.challenged[target] == sender) {
                            Duel(sender, target, Data.challengeKit[target]!!, Data.getFreeGameID()).start()
                        }
                    } else {
                        player.sendLocalizedMessage(Localization.PLAYER_NOT_ONLINE_DE.replace("%playerName%", args[2]),
                            Localization.PLAYER_NOT_ONLINE_EN.replace("%playerName%", args[2]))
                    }
                } else {
                    player.sendLocalizedMessage(Localization.COMMAND_WRONG_ARGUMENTS_DE, Localization.COMMAND_WRONG_ARGUMENTS_EN)
                    player.sendMessage(Localization.CHALLENGE_COMMAND_HELP)
                }
            } else {
                player.sendLocalizedMessage(Localization.CANT_DO_THAT_RIGHT_NOW_DE, Localization.CANT_DO_THAT_RIGHT_NOW_EN)
            }
        } else {
            sender.info("Du musst ein Spieler sein.")
        }
        return false
    }

}
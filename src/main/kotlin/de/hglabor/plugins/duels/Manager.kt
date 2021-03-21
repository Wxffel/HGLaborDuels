package de.hglabor.plugins.duels

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.arenas.Arenas
import de.hglabor.plugins.duels.commands.*
import de.hglabor.plugins.duels.data.DataHolder
import de.hglabor.plugins.duels.data.PlayerSettings
import de.hglabor.plugins.duels.data.PlayerStats
import de.hglabor.plugins.duels.database.MongoManager
import de.hglabor.plugins.duels.events.listeners.*
import de.hglabor.plugins.duels.events.listeners.arena.CreateArenaListener
import de.hglabor.plugins.duels.events.listeners.arena.OnChunkUnload
import de.hglabor.plugins.duels.events.listeners.duel.*
import de.hglabor.plugins.duels.events.listeners.soupsimulator.SoupsimulatorEvents
import de.hglabor.plugins.duels.functionality.EnderPearlFix
import de.hglabor.plugins.duels.functionality.SoupHealing
import de.hglabor.plugins.duels.guis.KitsGUI
import de.hglabor.plugins.duels.guis.PlayerSettingsGUI
import de.hglabor.plugins.duels.guis.overview.DuelPlayerDataOverviewGUI
import de.hglabor.plugins.duels.guis.overview.DuelTeamOverviewGUI
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.localization.Localization
import de.hglabor.plugins.duels.protection.Protection
import de.hglabor.plugins.duels.scoreboard.LobbyScoreboard
import de.hglabor.plugins.duels.spawn.SetSpawnCommand
import de.hglabor.plugins.duels.utils.CreateFiles
import de.hglabor.plugins.duels.utils.PlayerFunctions.reset
import de.hglabor.plugins.duels.utils.WorldManager
import de.hglabor.plugins.staff.commands.FollowCommand
import de.hglabor.plugins.staff.commands.StaffmodeCommand
import de.hglabor.plugins.staff.eventmanager.StaffOnInteract
import de.hglabor.plugins.staff.eventmanager.StaffOnInventoryClick
import de.hglabor.plugins.staff.eventmanager.StaffOnItemDrop
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.extensions.bukkit.success
import net.axay.kspigot.extensions.console
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.sound.sound
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import java.io.File


class Manager : KSpigot() {

    companion object {
        lateinit var INSTANCE: Manager; private set
        lateinit var mongoManager: MongoManager
    }

    override fun load() {
        INSTANCE = this

        console.info("Loading Duels plugin...")

        onlinePlayers.forEach {
            if (it.gameMode != GameMode.CREATIVE)
                it.reset()
            it.sound(Sound.BLOCK_BEACON_ACTIVATE)
        }

        WorldManager.deleteFightWorld()
        val duelsPath = File("plugins//HGLaborDuels//temp//duels//")
        if (duelsPath.exists()) {
            duelsPath.deleteRecursively()
        }
        File("plugins//HGLaborDuels//temp//duels//").mkdir()

        broadcast("enable kits in main")
        Kits.enable()

        broadcast("${Localization.PREFIX}${KColors.DODGERBLUE}ENABLED PLUGIN")

    }

    override fun startup() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
        register()
        LobbyScoreboard.startRunnable()
        CreateFiles
        connectMongo()
        WorldManager.createFightWorld()
        WorldManager.createBuildWorld()
        console.success("Duels plugin enabled.")
    }

    override fun shutdown() {
        broadcast("${Localization.PREFIX}${KColors.TOMATO}DISABLING PLUGIN ${KColors.DARKGRAY}(maybe a reload)")
        onlinePlayers.forEach {
            val playerStats = PlayerStats.get(it)
            playerStats.update()
            DataHolder.playerStats.remove(it)

            val playerSettings = PlayerSettings.get(it)
            playerSettings.update()
            DataHolder.playerSettings.remove(it)

            it.sound(Sound.BLOCK_BEACON_DEACTIVATE)
        }
        MongoManager.disconnect()
    }

    private fun register() {
        Localization.INSTANCE
        SoupHealing.enable()
        ArenaTags.enable()
        OnPlayerChat.enable()
        OnPlayerQuit.enable()
        OnPlayerJoin.enable()
        OnChallenge.enable()
        OnAccept.enable()
        OnDamage.enable()
        OnDeath.enable()
        OnFoodLevelChange.enable()
        OnInteractWithItem.enable()
        OnItemPickUp.enable()
        OnBuild.enable()
        OnPlayerCommandPreprocess.enable()
        OnInteractAtEntity.enable()
        OnInteractWithPressureplate.enable()
        OnDropItem.enable()
        OnWorldLoad.enable()
        OnPotionSplash.enable()
        Protection.enable()
        SoupsimulatorEvents.enable()
        CreateArenaListener.enable()
        OnChunkUnload.enable()
        OnBlockForm.enable()
        OnArrowPickUp.enable()
        OnDeathInDuel

        DuelPlayerDataOverviewGUI.enable()
        DuelTeamOverviewGUI.enable()
        KitsGUI.enable()
        //QueueGUI.enable()

        getCommand("challenge")!!.setExecutor(ChallengeCommand)
        getCommand("setspawn")!!.setExecutor(SetSpawnCommand)
        getCommand("spawn")!!.setExecutor(SpawnCommand)
        getCommand("arena")!!.setExecutor(ArenaCommand)
        getCommand("arena")!!.tabCompleter = ArenaCommand
        getCommand("spec")!!.setExecutor(SpecCommand)
        getCommand("spec")!!.tabCompleter = SpecCommand
        getCommand("stats")!!.setExecutor(StatsCommand)
        getCommand("dueloverview")!!.setExecutor(DuelOverviewCommand)
        getCommand("leave")!!.setExecutor(LeaveCommand)
        getCommand("party")!!.setExecutor(PartyCommand)
        getCommand("party")!!.tabCompleter = PartyCommand

        StaffOnItemDrop.enable()
        StaffOnInteract.enable()
        StaffOnInventoryClick.enable()
        getCommand("follow")!!.setExecutor(FollowCommand)
        getCommand("staffmode")!!.setExecutor(StaffmodeCommand)

        Arenas.enable()
        getCommand("tournament")!!.setExecutor(TournamentCommand)
        EnderPearlFix
    }

    private fun connectMongo() {
        mongoManager = MongoManager
        mongoManager.connect()
    }
}
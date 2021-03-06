package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.guis.QueueGUI
import de.hglabor.plugins.duels.kits.*
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Onebar : Kit(Kits.ONEBAR) {
    override val name = "Onebar"
    override val itemInGUIs = Kits.guiItem(Material.WOODEN_SWORD, name, "Soup")
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.SOUP
    override val specials = listOf(null)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.setItem(0, KitUtils.sword(Material.WOODEN_SWORD, false))

        for (i in 1..8) {
            if (player.inventory.getItem(i) == null) {
                player.inventory.setItem(i, ItemStack(Material.MUSHROOM_STEW))
            }
        }
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        kitMap[kits] = this
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs))
    }
}
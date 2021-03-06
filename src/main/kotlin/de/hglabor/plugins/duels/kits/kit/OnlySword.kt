package de.hglabor.plugins.duels.kits.kit

import de.hglabor.plugins.duels.arenas.ArenaTags
import de.hglabor.plugins.duels.guis.ChooseKitGUI
import de.hglabor.plugins.duels.guis.QueueGUI
import de.hglabor.plugins.duels.kits.Kit
import de.hglabor.plugins.duels.kits.KitType
import de.hglabor.plugins.duels.kits.Kits
import de.hglabor.plugins.duels.kits.kitMap
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class OnlySword : Kit(Kits.ONLYSWORD) {
    override val name = "Only Sword"
    override val itemInGUIs = Kits.guiItem(Material.GOLDEN_SWORD, name, null)
    override val arenaTag = ArenaTags.NONE
    override val type = KitType.NONE
    override val specials = listOf(null)

    override fun giveKit(player: Player) {
        player.inventory.clear()
        player.inventory.addItem(itemStack(Material.GOLDEN_SWORD) {
            meta {
                addEnchant(Enchantment.DAMAGE_ALL, 1, false)
                isUnbreakable = true
                flag(ItemFlag.HIDE_UNBREAKABLE)
            }
        })

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 100.0
        player.health = 100.0
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
    }

    override fun enable() {
        kitMap[kits] = this
        ChooseKitGUI.addContent(ChooseKitGUI.KitsGUICompoundElement(itemInGUIs))
    }
}

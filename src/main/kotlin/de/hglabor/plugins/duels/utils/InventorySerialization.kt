import de.hglabor.plugins.duels.utils.KitUtils
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType


object InventorySerialization {
    fun serializeInventory(inventoryMap: MutableMap<Int, ItemStack>): String {
        var serializedInventory = String()

        for (slot in inventoryMap.keys) {
            val item = inventoryMap[slot]
            val serializedItemStack = SerializeItemStack(item, slot)
            serializedInventory += "$serializedItemStack;"
        }
        return serializedInventory
    }

    fun SerializeItemStack(itemStack: ItemStack?, slot: Int? = null): String {
        if (itemStack == null || itemStack.type == Material.AIR) {
            return ""
        }
        var serializedItemStack = String()

        // slot
        if (slot != null)
            serializedItemStack += "s=$slot"

        // material (type)
        val type = itemStack.type
        serializedItemStack +=
            if (!itemStack.hasMark("goldenhead")) "-t=$type"
            else "-t=GOLDENHEAD"

        // durability
        if (itemStack is Damageable && itemStack.hasDamage()) {
            serializedItemStack += "-d=${itemStack.damage}"
        }

        // amount
        val amount = itemStack.amount
        if (amount != 1)
            serializedItemStack += "-a=$amount"

        // enchantments
        val enchantments = itemStack.enchantments
        if (enchantments.isNotEmpty()) {
            enchantments.forEach { (enchantment, level) ->
                serializedItemStack += "-e=${enchantment.key}:$level"
            }
        }

        // potioneffects
        if (itemStack.itemMeta != null) {
            val itemMeta = itemStack.itemMeta ?: return serializedItemStack
            // potioneffect
            if (itemMeta is PotionMeta) {
                serializedItemStack += "-p=${itemMeta.basePotionData.type}:${itemMeta.basePotionData.isExtended}:${itemMeta.basePotionData.isUpgraded}"
            }
        }
        return serializedItemStack
    }

    fun deserializeInventory(string: String): MutableMap<Int, ItemStack> {
        val inventoryContents = mutableMapOf<Int, ItemStack>()
        val serializedBlocks = string.split(";").toMutableList()
        serializedBlocks.removeLast()

        // one block is one inventory slot
        serializedBlocks.forEach { block ->
            val deserializedBlock = deserializeItemStack(block)
            inventoryContents[deserializedBlock.first] = deserializedBlock.second
        }

        return inventoryContents
    }

    fun deserializeItemStack(block: String): Pair<Int, ItemStack> {
        var slot = -1
        lateinit var itemStack: ItemStack

        val attributes = block.split('-').toTypedArray()
        attributes.forEach { attribute ->
            val value = attribute.split('=')[1]
            when (attribute[0]) {
                's' -> slot = value.toInt()
                't' -> itemStack =
                    if (value != "GOLDENHEAD") ItemStack(Material.getMaterial(value)!!)
                    else KitUtils.goldenHead(1)
                'd' -> {
                    val itemMeta = itemStack.itemMeta
                    if (itemMeta is Damageable) {
                        itemMeta.damage = value.toInt()
                    }
                    itemStack.itemMeta = itemMeta
                }
                'a' -> itemStack.amount = value.toInt()
                'e' -> {
                    val enchantmentName = NamespacedKey(value.split(':')[0], value.split(':')[1])
                    val enchantmentLevel = value.split(':')[2].toInt()
                    val enchantment = Enchantment.getByKey(enchantmentName)
                    if (enchantment != null) {
                        itemStack.addUnsafeEnchantment(enchantment, enchantmentLevel)
                    }
                }
                'n' -> itemStack.itemMeta?.setDisplayName(value)
                'p' -> {
                    val itemMeta = itemStack.itemMeta as PotionMeta
                    val potionDataAttributes = value.split(':')
                    val potionType = PotionType.valueOf(potionDataAttributes[0])
                    val isExtended = potionDataAttributes[1].toBoolean()
                    val isUpgraded = potionDataAttributes[2].toBoolean()
                    itemMeta.basePotionData = PotionData(potionType, isExtended, isUpgraded)
                    itemStack.itemMeta = itemMeta
                }
            }
        }
        return slot to itemStack
    }
}
package me.gharmazem.inventories;

import me.gharmazem.Main;
import me.gharmazem.utils.ColorUtils;
import me.gharmazem.manager.InventoryButton;
import me.gharmazem.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArmazemSection {

    public static final Map<String, InventoryButton> inventoryButtons = new LinkedHashMap<>();
    public static final Inventory inventory;

    static {
        FileConfiguration config = Main.getInstance().getConfig();
        String invname = config.getString("StorageInventory.inventory-name");
        int invslot = config.getInt("StorageInventory.inventory-size");
        int arrowslot = config.getInt("ArrowBack.slot");
        boolean arrowenable = config.getBoolean("ArrowBack.enable");

        inventory = Bukkit.createInventory(null, invslot, ColorUtils.colored(invname));
        ConfigurationSection itemSection = Main.getInstance().getConfig().getConfigurationSection("items");

        for (Iterator<String> iterator = itemSection.getKeys(false).iterator(); iterator.hasNext(); ) {
            String key = iterator.next();

            Material material = Material.valueOf(itemSection.getString(key + ".material"));
            String name = itemSection.getString(key + ".name");
            List<String> lore = itemSection.getStringList(key + ".lore");
            int price = itemSection.getInt(key + ".price");
            int slot = itemSection.getInt(key + ".slot");

            ItemBuilder item = (new ItemBuilder(material)).changeItemMeta(itemMeta -> {
                itemMeta.setDisplayName(ColorUtils.colored(name));
                itemMeta.setLore(ColorUtils.colored(lore));
            }).setNBTTag("itemsnbt", key);

            inventoryButtons.put(key, new InventoryButton(key, item.wrap(), slot, price));
        }

        if(arrowenable) {
            inventory.setItem(arrowslot, ArmazemItens.arrowBack());
        }
        inventory.setItem(10, ArmazemItens.storageDropsItem());
        inventoryButtons.forEach((k, button) -> inventory.setItem(button.getSlot(), button.getItemStack()));
    }

    public static Inventory getArmazemInventory() {
        return inventory;
    }
}
package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.commands.MultiWorldsCommands;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.WorldsListListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldsListView {

    private WorldsListView() {
    }

    public static Inventory setUp() {
        InventoryManager inventoryManager = new InventoryManager(null, 27, UltiTools.languageUtils.getString("world_page_title"), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager, new WorldsListListener());
        for (ItemStackManager itemStackManager : setUpItem()) {
            inventoryManager.addItem(itemStackManager);
        }
        return inventoryManager.getInventory();
    }

    private static List<ItemStackManager> setUpItem() {
        YamlConfiguration config = ConfigController.getConfig("worlds");

        List<ItemStackManager> itemStackManagers = new ArrayList<>();
        List<String> worlds = new ArrayList<>();
        for (World world : UltiTools.getInstance().getServer().getWorlds()) {
            if (world.getName().equalsIgnoreCase("world_nether")) {
                worlds.add("Nether");
            } else if (world.getName().equalsIgnoreCase("world_the_end")) {
                worlds.add("End");
            } else if (world.getName().equalsIgnoreCase("world")) {
                worlds.add("World");
            } else {
                worlds.add(world.getName());
            }
        }

        List<String> blockedWorlds = config.getStringList("blocked_worlds");
        MultiWorldsCommands.replaceOriginalWorldName(blockedWorlds);
        for (String world : worlds) {
            if (config.getString("world." + world + ".type") == null) {
                config.set("world." + world + ".alias", world);
                config.set("world." + world + ".type", "GRASS_BLOCK");
                config.set("world." + world + ".describe", "None");
                try {
                    config.save(ConfigsEnum.WORLDS.toString());
                } catch (IOException ignored) {
                }
            }
            ArrayList<String> lore = new ArrayList<>();
            String description = config.getString("world." + world + ".describe");
            lore.add(ChatColor.YELLOW + (description == null ? "None" : description));
            if (blockedWorlds.contains(world)) {
                lore.add(ChatColor.RED + UltiTools.languageUtils.getString("world_page_description_teleport_denied"));
            }
            String type = config.getString("world." + world + ".type");
            if (type == null) {
                type = "GRASS_BLOCK";
            }
            Material material = Material.getMaterial(type);
            ItemStack worldMaterial;
            if (material == null) {
                worldMaterial = UltiTools.versionAdaptor.getGrassBlock();
            } else {
                worldMaterial = new ItemStack(material);
            }
            String aliasName = config.getString("world." + world + ".alias");
            if (aliasName == null){
                config.set("world." + world + ".alias", world);
                try {
                    config.save(ConfigsEnum.WORLDS.toString());
                } catch (IOException ignored) {
                }
            }
            aliasName = (aliasName == null ? world : aliasName.replaceAll("&", "§"));
            ItemStackManager itemStackManager = new ItemStackManager(worldMaterial, lore, aliasName);
            itemStackManagers.add(itemStackManager);
        }
        return itemStackManagers;
    }
}

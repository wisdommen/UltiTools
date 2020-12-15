package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.commands.MultiWorldsCommands;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldsListView {

    private WorldsListView(){}

    public static Inventory setUp(){
        InventoryManager inventoryManager = new InventoryManager(null, 27, UltiTools.languageUtils.getString("world_page_title"), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager, new WorldsListListener());
        for(ItemStackManager itemStackManager : setUpItem()){
            inventoryManager.addItem(itemStackManager);
        }
        return inventoryManager.getInventory();
    }

    private static List<ItemStackManager> setUpItem(){
        File file = new File(ConfigsEnum.WORLDS.toString());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

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
        for (String world : worlds){
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW+config.getString("world."+world+".describe"));
            if (blockedWorlds.contains(world)){
                lore.add(ChatColor.RED+UltiTools.languageUtils.getString("world_page_description_teleport_denied"));
            }
            Material material = Material.getMaterial(Objects.requireNonNull(config.getString("world." + world + ".type")));
            ItemStack worldMaterial;
            if (material==null){
                worldMaterial = UltiTools.versionAdaptor.getGrassBlock();
            }else {
                worldMaterial = new ItemStack(material);
            }
            ItemStackManager itemStackManager = new ItemStackManager(worldMaterial, lore, world);
            itemStackManagers.add(itemStackManager);
        }
        return itemStackManagers;
    }
}

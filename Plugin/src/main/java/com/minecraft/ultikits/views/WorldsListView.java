package com.minecraft.ultikits.views;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.ViewManager;
import com.minecraft.ultikits.inventoryapi.ViewType;
import com.minecraft.ultikits.listener.WorldsListListener;
import com.minecraft.ultikits.manager.ItemStackManager;
import com.minecraft.ultikits.ultitools.UltiTools;
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
        InventoryManager inventoryManager = new InventoryManager(null, 27, "世界列表", true);
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
        if (blockedWorlds.contains("world_the_end")){
            blockedWorlds.remove("world_the_end");
            blockedWorlds.add("End");
        }
        if (blockedWorlds.contains("world_nether")){
            blockedWorlds.remove("world_nether");
            blockedWorlds.add("Nether");
        }
        if (blockedWorlds.contains("world")){
            blockedWorlds.remove("world");
            blockedWorlds.add("World");
        }
        for (String world : worlds){
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW+config.getString("world."+world+".describe"));
            if (blockedWorlds.contains(world)){
                lore.add(ChatColor.RED+"已被禁止传送进入！");
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

package com.ultikits.ultitools.views;

import com.ultikits.enums.Colors;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.commands.HomeCommands;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.services.HomeService;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HomeListView {

    private static final List<Colors> beds = Arrays.asList(Colors.values());

    private HomeListView() {
    }

    public static Inventory setUp(Player player) {
        InventoryManager inventoryManager = new InventoryManager(null, 36, player.getName() + UltiTools.languageUtils.getString("home_'s_home_list"), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStackManager home : setUpItems(player)){
                    inventoryManager.addItem(home);
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
        return inventoryManager.getInventory();
    }

    private static List<ItemStackManager> setUpItems(Player player) {
        List<ItemStackManager> itemStackManagers = new ArrayList<>();
        List<String> homeNames = HomeService.getHomeList(player);
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String each : homeNames) {
            ArrayList<String> lore = new ArrayList<>();
            String homeName = each;
            if (each.equals(UltiTools.languageUtils.getString("default"))) {
                each = "Def";
            }
            String path = player.getName() + "." + each;
            Location location = HomeService.getLocation(config, path);
            String world = String.format(ChatColor.YELLOW + UltiTools.languageUtils.getString("home_page_home_description_in_world")+" %s", location.getWorld().getName());
            String xyz = String.format(ChatColor.GRAY + "X: %.2f Y: %.2f Z: %.2f", location.getX(), location.getY(), location.getZ());
            lore.add(world);
            lore.add(xyz);
            Random random = new Random();
            ItemStackManager itemStackManager = new ItemStackManager(UltiTools.versionAdaptor.getBed(beds.get(random.nextInt(beds.size()))), lore, homeName);
            itemStackManagers.add(itemStackManager);
        }
        return itemStackManagers;
    }
}

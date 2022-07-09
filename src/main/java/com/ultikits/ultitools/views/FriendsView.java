package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.services.DatabasePlayerService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class FriendsView {

    private FriendsView(){
    }

    public static Inventory setUp(Player player){
        InventoryManager inventoryManager = new InventoryManager(null, 54, UltiTools.languageUtils.getString("friend_list")+" - "+ player.getName(), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager);
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> friendList = DatabasePlayerService.getFriendList(player);
                for (ItemStackManager itemStackManager : setUpFriends(friendList)){
                    inventoryManager.addItem(itemStackManager);
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
        return inventoryManager.getInventory();
    }

    private static List<ItemStackManager> setUpFriends(List<String> friends){
        List<ItemStackManager> itemStackManagers = new ArrayList<>();
        for (String friend : friends) {
            ArrayList<String> lore = new ArrayList<>();
            if (Bukkit.getPlayer(friend)==null){
                lore.add(ChatColor.YELLOW+UltiTools.languageUtils.getString("friend_state")+ChatColor.GRAY+UltiTools.languageUtils.getString("state_offline"));
            }else {
                Player player = Bukkit.getPlayer(friend);
                lore.add(ChatColor.YELLOW+UltiTools.languageUtils.getString("friend_state")+ChatColor.GREEN+UltiTools.languageUtils.getString("state_online"));
                GameMode gameMode = player.getGameMode();
                switch (gameMode){
                    case CREATIVE:
                        lore.add(ChatColor.YELLOW+UltiTools.languageUtils.getString("friend_mode") +ChatColor.GREEN
                                +UltiTools.languageUtils.getString("mode_creative"));
                        break;
                    case SURVIVAL:
                        lore.add(ChatColor.YELLOW+UltiTools.languageUtils.getString("friend_mode")+ChatColor.GREEN
                                +UltiTools.languageUtils.getString("mode_survival"));
                        break;
                    case ADVENTURE:
                        lore.add(ChatColor.YELLOW+UltiTools.languageUtils.getString("friend_mode")+ChatColor.GREEN
                                +UltiTools.languageUtils.getString("mode_adventure"));
                        break;
                    case SPECTATOR:
                        lore.add(ChatColor.YELLOW+UltiTools.languageUtils.getString("friend_mode")+ChatColor.GREEN
                                +UltiTools.languageUtils.getString("mode_spectator"));
                        break;
                }
                lore.add(ChatColor.AQUA + UltiTools.languageUtils.getString("friend_teleport"));
                lore.add(ChatColor.AQUA + UltiTools.languageUtils.getString("friend_tell"));
            }
            ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.PLAYER_HEAD), lore, friend);
            itemStackManagers.add(itemStackManager);
        }
        return itemStackManagers;
    }
}

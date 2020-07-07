package com.minecraft.ultikits.remoteChest;

import com.minecraft.ultikits.GUIs.GUISetup;
import com.minecraft.ultikits.GUIs.ItemStackManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.minecraft.ultikits.utils.EnchantItems.getRandomEnchantBook;

public class RemoteBagCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (command.getName().equals("bag")){
                GUISetup.setPlayerRemoteChest(player);
                player.openInventory(GUISetup.inventoryMap.get("chest").getInventory());
                return true;
            }
            else if (command.getName().equals("book")){
                getRandomEnchantBook();
                return true;
            }
            else if (command.getName().equals("test")){
                ArrayList<String> lore = new ArrayList<>();
                lore.add("lore1");
                lore.add("lore2");
                lore.add("lore3");
                ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.DIAMOND_AXE, 1), lore, ChatColor.RED+"我的小斧子");
                itemStackManager.setUpItem();
                player.getInventory().addItem(itemStackManager.getItem());
            }
        }
        return false;
    }
}

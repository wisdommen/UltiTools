package com.minecraft.ultikits.utils;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class DeathPunishUtils {
    public static void takeMoney(Player player, int money) {
        EconomyUtils.withdraw(player, Math.min(EconomyUtils.checkMoney(player), money));
    }

    public static void Exec(List<String> cmd, String playerName) {
        for (String s : cmd) {
            UltiTools.getInstance().getServer().dispatchCommand(
                    UltiTools.getInstance().getServer().getConsoleSender(), s.replace("{PLAYER}", playerName)
            );
        }
    }

    public static void takeItem(Player player, int drop) {
        Random random = new Random();
        Inventory inventory = player.getInventory();
        int count = 1;
        if (inventory.getContents().length != 0) {
            while (count < drop) {
                ItemStack itemStack = player.getInventory().getItem(random.nextInt(45));
                if (itemStack != null) {
                    itemStack.setAmount(1);
                    inventory.remove(itemStack);
                    count++;
                }
                if(inventory.getContents().length == 0) {
                    break;
                }
            }
        }
    }
}
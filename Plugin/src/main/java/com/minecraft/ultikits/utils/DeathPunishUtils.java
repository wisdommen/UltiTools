package com.minecraft.ultikits.utils;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class DeathPunishUtils {
    public static void takeMoney(Player player, int money) {
        EconomyUtils.withdraw(player, Math.min(EconomyUtils.checkMoney(player), money));
    }

    public static void Exec(String cmd) {
        UltiTools.getInstance().getServer().dispatchCommand(UltiTools.getInstance().getServer().getConsoleSender(), cmd);
    }

    public static void Exec(Player player, String cmd) {
        UltiTools.getInstance().getServer().dispatchCommand(player, cmd);
    }

    public static void takeItem(Player player, int times) {
        Random random = new Random();
        Inventory inventory = player.getInventory();
        int count = 1;
        if (inventory.getContents().length != 0) {
            while (count < times) {
                ItemStack itemStack = player.getInventory().getItem(random.nextInt(45));
                if (itemStack != null) {
                    itemStack.setAmount(random.nextInt(itemStack.getAmount()));
                    inventory.remove(itemStack);
                    count++;
                }
            }
        }
    }
}

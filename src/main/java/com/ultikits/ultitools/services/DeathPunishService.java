package com.ultikits.ultitools.services;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.EconomyUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author qianmo, wisdomme
 */
public class DeathPunishService {
    public static void takeMoney(Player player, int money) {
        if (EconomyUtils.checkMoney(player) >= money) {
            EconomyUtils.withdraw(player, Math.min(EconomyUtils.checkMoney(player), money));
        }else {
            EconomyUtils.withdraw(player, EconomyUtils.checkMoney(player));
        }
    }

    public static void Exec(List<String> cmd, String playerName) {
        for (String s : cmd) {
            UltiTools.getInstance().getServer().dispatchCommand(
                    UltiTools.getInstance().getServer().getConsoleSender(), s.replace("{PLAYER}", playerName)
            );
        }
    }

    public static void takeItem(Player player, int drop, List<String> whitelist) {
        Inventory inventory = player.getInventory();
        List<Integer> inventorySlot = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            ItemStack item = inventory.getItem(i);
            if (item!=null && !whitelist.contains(item.getType().toString())) inventorySlot.add(i);
        }
        Collections.shuffle(inventorySlot);
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < drop; i++) {
            if (i >= inventorySlot.size()) break;
            ints.add(inventorySlot.get(i));
        }
        for (Integer slot : ints){
            ItemStack itemStack = inventory.getItem(slot);
            if (itemStack == null) continue;
            itemStack.setAmount(itemStack.getAmount()-1);
            inventory.setItem(slot, itemStack);
        }
    }
}

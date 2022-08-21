package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@CmdExecutor(permission = "ultikits.tools.lore", description = "lore_function", alias = "lore")
public class LoreCommands extends AbstractTabExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (strings.length < 2) return false;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lore_no_item"));
            return true;
        }
        ItemMeta itemMeta = Objects.requireNonNull(itemStack.getItemMeta());
        List<String> lore = itemMeta.getLore();
        switch (strings[0]) {
            case "delete" :
                if (strings.length != 2) return false;
                if (lore == null || lore.isEmpty()) {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lore_empty"));
                    return true;
                }
                lore.remove(Integer.parseInt(strings[1]) - 1);
                break;
            case "add" :
                List<String> content = new ArrayList<>(Arrays.asList(strings));
                content.remove("add");
                if (lore == null || lore.isEmpty()) {
                    lore = content;
                } else {
                    lore.addAll(content);
                }
                break;
            case "edit" :
                if (Objects.equals(strings[1], "0")) {
                    itemMeta.setDisplayName(strings[2]);
                } else {
                    if (lore == null || lore.isEmpty()) {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lore_empty"));
                        return true;
                    }
                    if (strings.length != 3) return false;
                    lore.add(Integer.parseInt(strings[1]) - 1, strings[2]);
                }
                break;
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        player.getInventory().setItemInMainHand(itemStack);
        player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("lore_changed"));
        return true;
    }

    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (strings.length == 1) {
            return Arrays.asList(
                    "add",
                    "remove",
                    "edit"
            );
        } else {
            return null;
        }
    }
}

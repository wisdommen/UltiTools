package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.ultikits.utils.MessagesUtils.info;
import static com.ultikits.utils.MessagesUtils.warning;

public class LoreCommands extends AbstractTabExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!player.isOp() && !player.hasPermission("ultikits.tools.command.lore")) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
            return true;
        } else if (strings.length > 2) {
            if (strings[0].equalsIgnoreCase("add")) {
                List<String> newLore = Arrays.asList(strings);
                newLore.remove(0);
                for (int i = 0; i < newLore.size(); i++) newLore.set(i, newLore.get(i).replace("&", "§"));
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType() == Material.AIR) {
                    player.sendMessage(warning(UltiTools.languageUtils.getString("lore_no_item")));
                    return true;
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = Objects.requireNonNull(itemMeta).getLore();
                Objects.requireNonNull(lore).addAll(newLore);
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                player.sendMessage(info(UltiTools.languageUtils.getString("lore_added")));
                return true;
            }
            if (strings[0].equalsIgnoreCase("set")) {
                List<String> lore = Arrays.asList(strings);
                lore.remove(0);
                for (int i = 0; i < lore.size(); i++) lore.set(i, lore.get(i).replace("&", "§"));
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType() == Material.AIR) {
                    player.sendMessage(warning(UltiTools.languageUtils.getString("lore_no_item")));
                    return true;
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                Objects.requireNonNull(itemMeta).setLore(lore);
                itemStack.setItemMeta(itemMeta);
                player.sendMessage(info(UltiTools.languageUtils.getString("lore_set")));
                return true;
            }
            return false;
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("remove")) {
            Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
            if (!pattern.matcher(strings[1]).matches()) {
                player.sendMessage(warning(UltiTools.languageUtils.getString("lore_must_be_number")));
                return true;
            }
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                player.sendMessage(warning(UltiTools.languageUtils.getString("lore_no_item")));
                return true;
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = Objects.requireNonNull(itemMeta).getLore();
            if (Integer.parseInt(strings[1]) > Objects.requireNonNull(lore).size()) {
                player.sendMessage(warning(UltiTools.languageUtils.getString("lore_not_found")));
                return true;
            }
            lore.remove(Integer.parseInt(strings[1]));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            player.sendMessage(info(UltiTools.languageUtils.getString("lore_removed")));
            return true;
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("clear")) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                player.sendMessage(warning(UltiTools.languageUtils.getString("lore_no_item")));
                return true;
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = Objects.requireNonNull(itemMeta).getLore();
            Objects.requireNonNull(lore).clear();
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            player.sendMessage(info(UltiTools.languageUtils.getString("lore_cleared")));
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> tabs = Collections.emptyList();
        if (strings.length == 1) tabs = Arrays.asList("add", "set", "clear", "remove");
        return tabs;
    }
}
package com.minecraft.ultikits.api;

import com.minecraft.ultikits.enums.Sounds;
import com.minecraft.ultikits.enums.Colors;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public interface VersionWrapper {

    ItemStack getColoredPlaneGlass(Colors plane);

    ItemStack getSign();

    ItemStack getEndEye();

    ItemStack getEmailMaterial(boolean isRead);

    ItemStack getHead(OfflinePlayer player);

    ItemStack getGrassBlock();

    Objective registerNewObjective(Scoreboard scoreboard, String name, String criteria, String displayName);

    Sound getSound(Sounds sound);

    ItemStack getBed(Colors bedColor);

    int getItemDurability(ItemStack itemStack);

    ItemStack getItemInHand(Player player, boolean isMainHand);
}

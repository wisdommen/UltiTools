package com.minecraft.ultikits.beans;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

/**
 * @author wisdomme
 */
public class ArmorsBean {
    private ItemStack hat;
    private ItemStack chest;
    private ItemStack leg;
    private ItemStack boots;
    private ItemStack mainHand;
    private ItemStack secondaryHand;

    public ArmorsBean(Player player) {
        if (player.getInventory().getHelmet() != null) {
            hat = player.getInventory().getHelmet();
        }
        if (player.getInventory().getChestplate() != null) {
            chest = player.getInventory().getChestplate();
        }
        if (player.getInventory().getLeggings() != null) {
            leg = player.getInventory().getLeggings();
        }
        if (player.getInventory().getBoots() != null) {
            boots = player.getInventory().getBoots();
        }
        if (UltiTools.versionAdaptor.getItemInHand(player, true).getType() != Material.AIR) {
            mainHand = UltiTools.versionAdaptor.getItemInHand(player, true);
        }
        if (UltiTools.versionAdaptor.getItemInHand(player, false) != null && UltiTools.versionAdaptor.getItemInHand(player, false).getType() != Material.AIR) {
            secondaryHand = UltiTools.versionAdaptor.getItemInHand(player, false);
        }
    }

    public int getHatDurability() {
        try {
            return UltiTools.versionAdaptor.getItemDurability(this.hat);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getChestDurability() {
        try {
            return UltiTools.versionAdaptor.getItemDurability(this.chest);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getLegDurability() {
        try {
            return UltiTools.versionAdaptor.getItemDurability(this.leg);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getBootsDurability() {
        try {
            return UltiTools.versionAdaptor.getItemDurability(this.boots);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getMainHandDurability() {
        try {
            return UltiTools.versionAdaptor.getItemDurability(this.mainHand);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getOffHandDurability() {
        try {
            return UltiTools.versionAdaptor.getItemDurability(this.secondaryHand);
        } catch (NullPointerException e) {
            return -1;
        }
    }

}

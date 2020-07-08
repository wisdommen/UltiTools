package com.minecraft.ultikits.scoreBoard;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

/**
 * @author wisdomme
 */
public class ArmorsManager {
    private ItemStack hat;
    private ItemStack chest;
    private ItemStack leg;
    private ItemStack boots;
    private ItemStack mainHand;
    private ItemStack secondaryHand;

    public ArmorsManager(Player player) {
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
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            mainHand = player.getInventory().getItemInMainHand();
        }
        if (player.getInventory().getItemInOffHand().getType() != Material.AIR) {
            secondaryHand = player.getInventory().getItemInOffHand();
        }
    }

    public int getHatDurability() {
        try {
            return this.hat.getType().getMaxDurability() - ((Damageable) this.hat.getItemMeta()).getDamage();
        } catch (NullPointerException e){
            return -1;
        }
    }

    public int getChestDurability() {
        try {
            return this.chest.getType().getMaxDurability() - ((Damageable) this.chest.getItemMeta()).getDamage();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getLegDurability() {
        try {
            return this.leg.getType().getMaxDurability() - ((Damageable) this.leg.getItemMeta()).getDamage();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getBootsDurability() {
        try {
            return this.boots.getType().getMaxDurability() - ((Damageable) this.boots.getItemMeta()).getDamage();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getMainHandDurability() {
        try {
            return this.mainHand.getType().getMaxDurability() - ((Damageable) this.mainHand.getItemMeta()).getDamage();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int getOffHandDurability() {
        try {
            return this.secondaryHand.getType().getMaxDurability() - ((Damageable) this.secondaryHand.getItemMeta()).getDamage();
        } catch (NullPointerException e) {
            return -1;
        }
    }

}

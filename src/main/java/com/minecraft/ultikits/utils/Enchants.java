package com.minecraft.ultikits.utils;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

public class Enchants {

    public static Enchantment getEnchantment(String enchantment) {
        enchantment = enchantment.toLowerCase().replaceAll("[ _-]", "");

        Map<String, String> diff_namings = new HashMap<>();
        diff_namings.put("aspectfire", "fireaspect");
        diff_namings.put("sharpness", "damageall");
        diff_namings.put("smite", "damageundead");
        diff_namings.put("punch", "arrowknockback");
        diff_namings.put("looting", "lootbonusmobs");
        diff_namings.put("fortune", "lootbonusblocks");
        diff_namings.put("baneofarthropods", "damageundead");
        diff_namings.put("power", "arrowdamage");
        diff_namings.put("flame", "arrowfire");
        diff_namings.put("infinity", "arrowinfinite");
        diff_namings.put("unbreaking", "durability");
        diff_namings.put("efficiency", "digspeed");
        diff_namings.put("sweeping", "sweepingedge");
        diff_namings.put("respiration", "oxygen");
        diff_namings.put("protection", "protectionenvironmental");
        diff_namings.put("blastprotection", "protectionexplosions");
        diff_namings.put("mobloot", "lootbonusmobs");
        diff_namings.put("projectileprotection", "protectionprojectile");
        //diff_namings.put("fireprotection", "protectionfire");
        diff_namings.put("fallprot", "protectionfall");
        diff_namings.put("infarrows", "arrowinfinite");

        String change = diff_namings.get(enchantment);
        if (change != null) {
            enchantment = change;
        }
        for (Enchantment value : Enchantment.values()) {
            if (value.getKey().toString().replaceAll("[ _-]", "").contains(enchantment)) {
                return value;
            }
        }
        return null;
    }


}

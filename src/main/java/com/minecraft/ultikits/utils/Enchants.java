package com.minecraft.ultikits.utils;

import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

public class Enchants {

    private Enchants(){}

    public static Enchantment getEnchantment(String enchantment) {
        enchantment = enchantment.toLowerCase().replaceAll("[ _-]", "");

        if (enchantment.equals("protection")){
            return Enchantment.PROTECTION_ENVIRONMENTAL;
        }
        for (Enchantment value : Enchantment.values()) {
            if (value.getKey().toString().replaceAll("[ _-]", "").contains(enchantment)) {
                return value;
            }
        }
        return null;
    }


}

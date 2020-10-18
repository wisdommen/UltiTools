package com.minecraft.ultikits.utils;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MessagesUtils {

    private MessagesUtils(){}

    @Contract(pure = true)
    public static @NotNull String info(String message) {
        return ChatColor.YELLOW + message;
    }

    @Contract(pure = true)
    public static @NotNull String warning(String message) {
        return ChatColor.RED + message;
    }

    @Contract(pure = true)
    public static @NotNull String unimportant(String message) {
        return ChatColor.GRAY + message;
    }


    public static String player_inventory_full = warning(UltiTools.languageUtils.getWords("player_inventory_full"));
    public static String not_enough_money = warning(UltiTools.languageUtils.getWords("not_enough_money"));
    public static String kit_already_claimed = warning(UltiTools.languageUtils.getWords("kit_already_claimed"));
    public static String claimed = info(UltiTools.languageUtils.getWords("claimed"));
    public static String bought = info(UltiTools.languageUtils.getWords("bought"));

}

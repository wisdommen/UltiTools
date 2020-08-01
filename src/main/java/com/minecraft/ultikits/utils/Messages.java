package com.minecraft.ultikits.utils;

import org.bukkit.ChatColor;

public class Messages {

    private Messages(){}

    public static String info(String message) {
        return ChatColor.YELLOW + message;
    }

    public static String warning(String message) {
        return ChatColor.RED + message;
    }

    public static String unimportant(String message) {
        return ChatColor.GRAY + message;
    }


    public static String player_inventory_full = warning("你的背包放不下啦，清理一下吧！");
    public static String not_enough_money = warning("你没有这么多钱哦！");
    public static String not_enough_space = warning("背包放不下啦！");
    public static String kit_already_claimed = warning("你已经领取过了！");
    public static String claimed = info("已领取");
    public static String bought = info("已购买");
    public static String change_mode_to_survival = warning("请切换到生存模式再点击！");
    public static String player_does_not_have_enough_items = warning("你没有那么多！");

}

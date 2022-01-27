package com.ultikits.ultitools.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.ultikits.enums.Colors;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.tasks.TradeTask;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.views.TradeView;
import com.ultikits.utils.EconomyUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TradeUtils {

    private static BiMap<String, String> inTradeMode = HashBiMap.create();
    private static BiMap<String, String> inRequestMode = HashBiMap.create();
    private static Map<String, Integer> tradeMoney = new HashMap<>();
    private static Map<String, Integer> tradeExp = new HashMap<>();
    private static List<String> tradeConfirm = new ArrayList<>();

    public static BiMap<String, String> getInTradeMode() {
        return inTradeMode;
    }

    public static void setInTradeMode(BiMap<String, String> inTradeMode) {
        TradeUtils.inTradeMode = inTradeMode;
    }

    public static BiMap<String, String> getInRequestMode() {
        return inRequestMode;
    }

    public static void setInRequestMode(BiMap<String, String> inRequestMode) {
        TradeUtils.inRequestMode = inRequestMode;
    }

    public static Map<String, Integer> getTradeMoney() {
        return tradeMoney;
    }

    public static void setTradeMoney(Map<String, Integer> tradeMoney) {
        TradeUtils.tradeMoney = tradeMoney;
    }

    public static Map<String, Integer> getTradeExp() {
        return tradeExp;
    }

    public static void setTradeExp(Map<String, Integer> tradeExp) {
        TradeUtils.tradeExp = tradeExp;
    }

    public static List<String> getTradeConfirm() {
        return tradeConfirm;
    }

    public static void setTradeConfirm(List<String> tradeConfirm) {
        TradeUtils.tradeConfirm = tradeConfirm;
    }

    public static void requestTrade(Player From, Player To) {
        inRequestMode.put(From.getName(), To.getName());
        From.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("trade_you_requested"), To.getName()));
        To.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("trade_request"), From.getName()));
        To.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("trade_request_tip"));
        To.sendMessage(ChatColor.GOLD + "================================");
        To.spigot().sendMessage(
                new ComponentBuilder(UltiTools.languageUtils.getString("trade_accept_btn"))
                        .color(net.md_5.bungee.api.ChatColor.GREEN)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/trade accept"))
                        .getCurrentComponent(),
                new ComponentBuilder("           ").getCurrentComponent(),
                new ComponentBuilder(UltiTools.languageUtils.getString("trade_reject_btn"))
                        .color(net.md_5.bungee.api.ChatColor.RED)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/trade reject"))
                        .getCurrentComponent()
        );
        To.sendMessage(ChatColor.GOLD + "================================");
        new TradeTask(From, To).runTaskTimerAsynchronously(UltiTools.getInstance(), 0L, 2L);
    }

    public static void acceptTrade(Player To) {
        Player From = getOtherParty(To);
        inTradeMode.put(From.getName(), To.getName());
        inRequestMode.remove(Objects.requireNonNull(From).getName());

        tradeMoney.put(From.getName(), 0);
        tradeMoney.put(To.getName(), 0);

        tradeExp.put(From.getName(), 0);
        tradeExp.put(To.getName(), 0);

        Inventory TradeGUIA = TradeView.setUp(From);
        Inventory TradeGUIB = TradeView.setUp(To);

        From.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_player_accepted"));
        To.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_you_accepted"));
        From.openInventory(TradeGUIA);
        To.openInventory(TradeGUIB);
    }

    public static void rejectTrade(Player To) {
        Player From = getOtherParty(To);
        removeRequest(From);
        From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_rejected"));
        To.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_you_rejected"));
    }

    public static void removeRequest(Player player) {
        inRequestMode.remove(player.getName());
        inRequestMode.inverse().remove(player.getName());
    }

    public static void closeTrade(Player From, Player To) {
        new BukkitRunnable() {
            final Inventory i1 = From.getOpenInventory().getTopInventory();
            final Inventory i2 = To.getOpenInventory().getTopInventory();
            final List<Integer> l1 = getItemPlacementArea(From);
            final List<Integer> l2 = getItemPlacementArea(To);
            @Override
            public void run() {
                for (int i : l1) {
                    if (i1.getItem(i) != null) From.getInventory().addItem(i1.getItem(i));
                }
                for (int n : l2) {
                    if (i2.getItem(n) != null) To.getInventory().addItem(i2.getItem(n));
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
        inTradeMode.remove(From.getName());
        if (From.isOnline()) {
            From.closeInventory();
            From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_closed"));
        }
        if (To.isOnline()) {
            To.closeInventory();
            To.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_closed"));
        }
    }

    public static void closeTrade(Player player) {
        if (getInTradeMode().containsKey(player.getName())) {
            closeTrade(player, getOtherParty(player));
            return;
        }
        if (getInTradeMode().containsValue(player.getName())) {
            closeTrade(getOtherParty(player), player);
        }
    }

    public static void confirmTrade(Player From, Player To) {
        new BukkitRunnable() {
            final Inventory i1 = From.getOpenInventory().getTopInventory();
            final Inventory i2 = To.getOpenInventory().getTopInventory();
            final List<Integer> l1 = getItemPlacementArea(From);
            final List<Integer> l2 = getItemPlacementArea(To);
            @Override
            public void run() {
                int t1 = 0;
                int t2 = 0;

                for (int i : l1) {
                    if (i1.getItem(i) != null) {
                        To.getInventory().addItem(i1.getItem(i));
                        t1 = t1+ 1;
                    }
                }
                for (int i : l2) {
                    if (i2.getItem(i) != null) {
                        From.getInventory().addItem(i2.getItem(i));
                        t2 = t2 +1;
                    }
                }

                if (isMoneyTradeAllowed()) {
                    EconomyUtils.deposit(From, tradeMoney.get(To.getName()));
                    EconomyUtils.deposit(To, tradeMoney.get(From.getName()));
                    EconomyUtils.withdraw(From, tradeMoney.get(From.getName()));
                    EconomyUtils.withdraw(To, tradeMoney.get(To.getName()));
                }

                if (isExpTradeAllowed()) {
                    Bukkit.getScheduler().callSyncMethod(UltiTools.getInstance(),() -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp add " + From.getName() + " " + (tradeExp.get(To.getName()) - tradeExp.get(From.getName())) + " points");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp add " + To.getName() + " " + (tradeExp.get(From.getName()) - tradeExp.get(To.getName())) + " points");
                        return true;
                    });
                }

                From.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_success"));
                From.sendMessage(ChatColor.GOLD + String.format(UltiTools.languageUtils.getString("trade_money_you_get"), tradeMoney.get(To.getName())));
                From.sendMessage(ChatColor.LIGHT_PURPLE+ String.format(UltiTools.languageUtils.getString("trade_exp_you_get"), tradeExp.get(To.getName())));
                From.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("trade_item_you_get"), t2));

                To.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_success"));
                To.sendMessage(ChatColor.GOLD + String.format(UltiTools.languageUtils.getString("trade_money_you_get"), tradeMoney.get(From.getName())));
                To.sendMessage(ChatColor.LIGHT_PURPLE+ String.format(UltiTools.languageUtils.getString("trade_exp_you_get"), tradeExp.get(From.getName())));
                To.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("trade_item_you_get"), t1));
            }
        }.runTaskAsynchronously(UltiTools.getInstance());

        inTradeMode.remove(From.getName());
        tradeConfirm.remove(From.getName());
        tradeConfirm.remove(To.getName());

        From.closeInventory();
        To.closeInventory();
    }

    public static void confirmTrade(Player player) {
        if (getInTradeMode().containsKey(player.getName())) {
            confirmTrade(player, getOtherParty(player));
            return;
        }
        if (getInTradeMode().containsValue(player.getName())){
            confirmTrade(getOtherParty(player), player);
        }
    }

    public static List<String> getMoneyLore(Player player) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_money_now") + " " + ChatColor.YELLOW + tradeMoney.get(player.getName()));
        list.add(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_your_money") + " " + ChatColor.YELLOW + EconomyUtils.checkMoney(player));
        if (inTradeMode.containsKey(player.getName())) list.add(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("trade_player_money") + " " + ChatColor.YELLOW + tradeMoney.get(inTradeMode.get(player.getName())));
        if (inTradeMode.containsValue(player.getName())) list.add(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("trade_player_money") + " " + ChatColor.YELLOW + tradeMoney.get(inTradeMode.inverse().get(player.getName())));
        return getBaseLore(list);
    }

    public static List<String> getExpLore(Player player) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_exp_now") + " " + ChatColor.YELLOW + tradeExp.get(player.getName()));
        list.add(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_your_exp") + " " + ChatColor.YELLOW + player.getTotalExperience());
        if (inTradeMode.containsKey(player.getName())) list.add(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("trade_player_exp") + " " + ChatColor.YELLOW + tradeExp.get(inTradeMode.get(player.getName())));
        if (inTradeMode.containsValue(player.getName())) list.add(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("trade_player_exp") + " " + ChatColor.YELLOW + tradeExp.get(inTradeMode.inverse().get(player.getName())));
        return getBaseLore(list);
    }

    public static List<String> getConfirmLore(Player player, boolean changed) {
        List<String> list = new ArrayList<>();
        if (tradeConfirm.contains(player.getName())) {
            list.add(ChatColor.GREEN + "√" + UltiTools.languageUtils.getString("trade_you_confirmed"));
        } else {
            list.add(ChatColor.RED + "×" + UltiTools.languageUtils.getString("trade_you_not_confirm"));
        }
        if (tradeConfirm.contains(getOtherParty(player).getName())) {
            list.add(ChatColor.GREEN + "√" + UltiTools.languageUtils.getString("trade_player_confirmed"));
        } else {
            list.add(ChatColor.RED + "×" + UltiTools.languageUtils.getString("trade_player_not_confirm"));
        }
        if (changed) {
            list.add(UltiTools.languageUtils.getString("trade_changed_need_reconfirm"));
        } else {
            list.add(UltiTools.languageUtils.getString("trade_if_changed"));
        }
        return list;
    }

    public static void updateMoneyLore(Player player, Inventory inventory, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setLore(getMoneyLore(player));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(48, itemStack);
    }

    public static void updateExpLore(Player player, Inventory inventory, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setLore(getExpLore(player));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(50, itemStack);
    }

    public static void updateConfirmLore(Player player, Inventory inventory, ItemStack itemStack, boolean changed) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setLore(getConfirmLore(player, changed));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(53, itemStack);
    }

    public static void updateAllMoneyLore(Player player, Inventory inventory, ItemStack itemStack) {
        updateMoneyLore(player, Objects.requireNonNull(inventory), Objects.requireNonNull(itemStack));
        updateMoneyLore(getOtherParty(player), Objects.requireNonNull(getOtherParty(player)).getOpenInventory().getTopInventory(), Objects.requireNonNull(itemStack));
    }

    public static void updateAllExpLore(Player player, Inventory inventory, ItemStack itemStack) {
        updateExpLore(player, Objects.requireNonNull(inventory), Objects.requireNonNull(itemStack));
        updateExpLore(getOtherParty(player), Objects.requireNonNull(getOtherParty(player)).getOpenInventory().getTopInventory(), Objects.requireNonNull(itemStack));
    }

    public static List<Integer> getBannedPosition() {
        return Arrays.asList(4, 13, 22, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
    }

    public static List<Integer> getItemPlacementArea(Player player) {
        if (inTradeMode.containsKey(player.getName())) {
            return Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30);
        } else {
            return Arrays.asList(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35);
        }
    }

    public static boolean isPlayerInRequestMode(Player player) {
        return inRequestMode.containsKey(player.getName()) || inRequestMode.containsValue(player.getName());
    }

    public static boolean isPlayerInTradeMode(Player player) {
        return inTradeMode.containsKey(player.getName()) || inTradeMode.containsValue(player.getName());
    }

    public static Player getOtherParty(Player player) {
        if (getInTradeMode().containsKey(player.getName())) {
            return Bukkit.getPlayerExact(inTradeMode.get(player.getName()));
        } else if (getInTradeMode().containsValue(player.getName())) {
            return Bukkit.getPlayerExact(inTradeMode.inverse().get(player.getName()));
        } else if (getInRequestMode().containsKey(player.getName())) {
            return Bukkit.getPlayerExact(inRequestMode.get(player.getName()));
        } else if (getInRequestMode().containsValue(player.getName())) {
            return Bukkit.getPlayerExact(inRequestMode.inverse().get(player.getName()));
        } else {
            return null;
        }
    }

    public static boolean isAllConfirmed(Player player) {
        return tradeConfirm.contains(player.getName()) && tradeConfirm.contains(TradeUtils.getOtherParty(player).getName());
    }

    public static void refreshConfirmation(Player player, Inventory inventory, ItemStack itemStack, boolean reset) {
        if (reset) {
            TradeUtils.getTradeConfirm().remove(player.getName());
            TradeUtils.getTradeConfirm().remove(TradeUtils.getOtherParty(player).getName());
        }
        TradeUtils.updateConfirmLore(player, inventory, itemStack, reset);
        TradeUtils.updateConfirmLore(
                TradeUtils.getOtherParty(player),
                TradeUtils.getOtherParty(player).getOpenInventory().getTopInventory(),
                itemStack,
                reset
        );
    }

    public static void refreshItem(Player player, int slot) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack itemStack = getOtherParty(player).getOpenInventory().getItem(slot);
                if (itemStack == null) {
                    ItemStack i = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.BROWN));
                    ItemMeta itemMeta = i.getItemMeta();
                    Objects.requireNonNull(itemMeta).setDisplayName(" ");
                    i.setItemMeta(itemMeta);
                    player.getOpenInventory().setItem(slot, i);
                } else {
                    player.getOpenInventory().setItem(slot, itemStack);
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());

    }

    public static void addTradeMoney(Player player, boolean tenfold) {
        if (!(EconomyUtils.checkMoney(player) > TradeUtils.getTradeMoney().get(player.getName()))) return;
        int money = tradeMoney.get(player.getName());
        if (tenfold) {
            money = money == 0 ? 1 : money * 10;
        } else {
            money = money + 1;
        }
        tradeMoney.put(player.getName(), money);
    }

    public static void addTradeExp(Player player, boolean tenfold) {
        if (!(player.getTotalExperience() > TradeUtils.getTradeExp().get(player.getName()))) return;
        int exp = tradeExp.get(player.getName());
        if (tenfold) {
            exp = exp == 0 ? 1 : exp * 10;
        } else {
            exp = exp + 1;
        }
        tradeExp.put(player.getName(), exp);
    }

    public static void reduceTradeMoney(Player player, boolean tenfold) {
        int money = tradeMoney.get(player.getName());
        if (tenfold) {
            money = money == 0 ? 0 : money / 10;
        } else {
            money = money == 0 ? 0 : money - 1;
        }
        tradeMoney.put(player.getName(), money);
    }

    public static void reduceTradeExp(Player player, boolean tenfold) {
        int exp = tradeExp.get(player.getName());
        if (tenfold) {
            exp = exp == 0 ? 0 : exp / 10;
        } else {
            exp = exp == 0 ? 0 : exp - 1;
        }
        tradeExp.put(player.getName(), exp);
    }

    public static boolean isCrossWorldTradeAllowed() {
        return ConfigController.getConfig("trade").getBoolean("allow_cross_world_trade");
    }

    public static boolean isMoneyTradeAllowed() {
        return ConfigController.getConfig("trade").getBoolean("enable_money_trade");
    }

    public static boolean isExpTradeAllowed() {
        return ConfigController.getConfig("trade").getBoolean("enable_exp_trade");
    }

    public static boolean isAnyConfirmed(Player player) {
        return tradeConfirm.contains(player.getName()) || tradeConfirm.contains(getOtherParty(player).getName());
    }

    public static void toggleTrade(Player player) {
        File playerFile = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        boolean toggle;
        if (playerData.isSet("trade")) {
            toggle = !playerData.getBoolean("trade");
        } else {
            toggle = false;
        }
        playerData.set("trade", toggle);
        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEnableTrade(Player player) {
        File playerFile = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        if (playerData.isSet("trade")) {
            return playerData.getBoolean("trade");
        } else {
            return true;
        }
    }

    public static boolean isBannedPlayer(Player master, Player player) {
        File playerFile = new File(ConfigsEnum.PLAYER.toString(), master.getName() + ".yml");
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        if (playerData.isSet("trade_banned")) {
            return false;
        } else {
            return playerData.getStringList("trade_banned").contains(player.getName());
        }
    }

    public static void addBannedPlayer(Player master, String player) {
        File playerFile = new File(ConfigsEnum.PLAYER.toString(), master.getName() + ".yml");
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        List<String> list;
        if (playerData.isSet("trade_banned")) {
            list = playerData.getStringList("trade_banned");
        } else {
            list = new ArrayList<>();
        }
        list.add(player);
        playerData.set("trade_banned", list);
        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeBannedPlayer(Player master, String player) {
        File playerFile = new File(ConfigsEnum.PLAYER.toString(), master.getName() + ".yml");
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        if (playerData.isSet("trade_banned")) {
            List<String> list = playerData.getStringList("trade_banned");
            list.remove(player);
            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> getBaseLore(List<String> list) {
        list.add(ChatColor.WHITE + UltiTools.languageUtils.getString("trade_left_click_add"));
        list.add(ChatColor.WHITE + UltiTools.languageUtils.getString("trade_right_click_reduce"));
        list.add(ChatColor.WHITE + UltiTools.languageUtils.getString("trade_shift_left_click_add"));
        list.add(ChatColor.WHITE + UltiTools.languageUtils.getString("trade_shift_right_click_reduce"));

        return list;
    }
}

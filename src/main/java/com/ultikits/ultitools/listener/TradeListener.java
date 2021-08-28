package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TradeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeListener implements Listener {

    private final int closeBtn = 45;
    private final int moneyBtn = 48;
    private final int expBtn = 50;
    private final int confirmBtn = 53;

    @EventHandler
    public void onPlayerClickPlayer (PlayerInteractAtEntityEvent event) {
        if (!UltiTools.getInstance().getConfig().getBoolean("enable_pro") || !UltiTools.getInstance().getProChecker().getProStatus()) return;
        if (!ConfigController.getConfig("trade").getBoolean("enable_shift_click_apply")) return;
        if (!(event.getRightClicked() instanceof Player)) return;
        if (!event.getPlayer().isSneaking()) return;

        Player From = event.getPlayer();
        Player To = (Player) event.getRightClicked();

        if (TradeUtils.getOtherParty(From).getName().equals(To.getName())) return;

        if (TradeUtils.isPlayerInTradeMode(To)) {
            From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_in_trading"));
            return;
        }

        if (!TradeUtils.isEnableTrade(To)) {
            From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_disabled"));
            return;
        }

        if (TradeUtils.isPlayerInRequestMode(From)) {
            From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_cannot_request_two_player"));
            return;
        }

        TradeUtils.requestTrade(From, To);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (TradeUtils.isPlayerInTradeMode(player)) {
            TradeUtils.closeTrade(player, TradeUtils.getOtherParty(player));
        }

        if (TradeUtils.isPlayerInRequestMode(player)) {
            TradeUtils.removeRequest(player);
        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase(UltiTools.languageUtils.getString("trade_title"))) return;
        if (!TradeUtils.isPlayerInTradeMode((Player) event.getPlayer())) return;
        if (TradeUtils.getInTradeMode().containsKey(event.getPlayer().getName())) {
            TradeUtils.closeTrade((Player) event.getPlayer(), TradeUtils.getOtherParty((Player) event.getPlayer()));
        } else {
            TradeUtils.closeTrade(TradeUtils.getOtherParty((Player) event.getPlayer()), (Player) event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!TradeUtils.isPlayerInTradeMode(event.getEntity())) return;
        if (TradeUtils.getInTradeMode().containsKey(event.getEntity().getName())) {
            TradeUtils.closeTrade(event.getEntity(), TradeUtils.getOtherParty(event.getEntity()));
        } else {
            TradeUtils.closeTrade(TradeUtils.getOtherParty(event.getEntity()), event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerClickItem (InventoryClickEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase(UltiTools.languageUtils.getString("trade_title"))) return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() != null) return;

        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        int slot = event.getSlot();

        if (TradeUtils.getBannedPosition().contains(slot)) {
            event.setCancelled(true);
            if (TradeUtils.isAllConfirmed(player)) return;
            switch (slot) {
                case closeBtn:
                    TradeUtils.refreshConfirmation(player, inventory, inventory.getItem(confirmBtn), true);
                    TradeUtils.closeTrade(player);
                    break;
                case moneyBtn:
                    if (event.isLeftClick()) {
                        TradeUtils.addTradeMoney(player, event.isShiftClick());
                    }
                    if (event.isRightClick()) {
                        TradeUtils.reduceTradeMoney(player, event.isShiftClick());
                    }
                    TradeUtils.updateAllMoneyLore(player, inventory, itemStack);
                    TradeUtils.refreshConfirmation(player, inventory, inventory.getItem(confirmBtn), true);
                    break;
                case expBtn:
                    if (event.isLeftClick()) {
                        TradeUtils.addTradeExp(player, event.isShiftClick());
                    }
                    if (event.isRightClick()) {
                        TradeUtils.reduceTradeExp(player, event.isShiftClick());
                    }
                    TradeUtils.refreshConfirmation(player, inventory, inventory.getItem(confirmBtn), true);
                    TradeUtils.updateAllExpLore(player, inventory, itemStack);
                    break;
                case confirmBtn:
                    TradeUtils.getTradeConfirm().add(player.getName());
                    TradeUtils.refreshConfirmation(player, inventory, itemStack, false);
                    if (!TradeUtils.isAllConfirmed(player)) return;
                    TradeUtils.confirmTrade(player);
                    break;
            }
        } else if (TradeUtils.getItemPlacementArea(player).contains(slot)) {
            if (TradeUtils.isAllConfirmed(player)) {
                event.setCancelled(true);
                return;
            }
            if (TradeUtils.isAnyConfirmed(player)) {
                TradeUtils.refreshConfirmation(player, inventory, inventory.getItem(confirmBtn), true);
            }
            TradeUtils.refreshItem(TradeUtils.getOtherParty(player), slot);
        } else {
            event.setCancelled(true);
        }
    }
}

package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.services.TradeService;
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

        if (TradeService.isPlayerInTradeMode(To)) {
            if (TradeService.getOtherParty(To).getName().equals(From.getName())) return;
            From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_in_trading"));
            return;
        }

        if (!TradeService.isEnableTrade(To)) {
            From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_disabled"));
            return;
        }

        if (TradeService.isPlayerInRequestMode(From)) {
            if (TradeService.getOtherParty(From).getName().equals(To.getName())) return;
            From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_cannot_request_two_player"));
            return;
        }

        TradeService.requestTrade(From, To);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (TradeService.isPlayerInTradeMode(player)) {
            TradeService.closeTrade(player, TradeService.getOtherParty(player));
        }

        if (TradeService.isPlayerInRequestMode(player)) {
            TradeService.removeRequest(player);
        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase(UltiTools.languageUtils.getString("trade_title"))) return;
        if (!TradeService.isPlayerInTradeMode((Player) event.getPlayer())) return;
        if (TradeService.getInTradeMode().containsKey(event.getPlayer().getName())) {
            TradeService.closeTrade((Player) event.getPlayer(), TradeService.getOtherParty((Player) event.getPlayer()));
        } else {
            TradeService.closeTrade(TradeService.getOtherParty((Player) event.getPlayer()), (Player) event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!TradeService.isPlayerInTradeMode(event.getEntity())) return;
        if (TradeService.getInTradeMode().containsKey(event.getEntity().getName())) {
            TradeService.closeTrade(event.getEntity(), TradeService.getOtherParty(event.getEntity()));
        } else {
            TradeService.closeTrade(TradeService.getOtherParty(event.getEntity()), event.getEntity());
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

        if (TradeService.getBannedPosition().contains(slot)) {
            event.setCancelled(true);
            if (TradeService.isAllConfirmed(player)) return;
            switch (slot) {
                case closeBtn:
                    TradeService.refreshConfirmation(player, inventory, inventory.getItem(confirmBtn), true);
                    TradeService.closeTrade(player);
                    break;
                case moneyBtn:
                    if (event.isLeftClick()) {
                        TradeService.addTradeMoney(player, event.isShiftClick());
                    }
                    if (event.isRightClick()) {
                        TradeService.reduceTradeMoney(player, event.isShiftClick());
                    }
                    TradeService.updateAllMoneyLore(player, inventory, itemStack);
                    TradeService.refreshConfirmation(player, inventory, inventory.getItem(confirmBtn), true);
                    break;
                case expBtn:
                    if (event.isLeftClick()) {
                        TradeService.addTradeExp(player, event.isShiftClick());
                    }
                    if (event.isRightClick()) {
                        TradeService.reduceTradeExp(player, event.isShiftClick());
                    }
                    TradeService.refreshConfirmation(player, inventory, inventory.getItem(confirmBtn), true);
                    TradeService.updateAllExpLore(player, inventory, itemStack);
                    break;
                case confirmBtn:
                    TradeService.getTradeConfirm().add(player.getName());
                    TradeService.refreshConfirmation(player, inventory, itemStack, false);
                    if (!TradeService.isAllConfirmed(player)) return;
                    TradeService.confirmTrade(player);
                    break;
            }
        } else if (TradeService.getItemPlacementArea(player).contains(slot)) {
            if (TradeService.isAllConfirmed(player)) {
                event.setCancelled(true);
                return;
            }
            if (TradeService.isAnyConfirmed(player)) {
                TradeService.refreshConfirmation(player, inventory, inventory.getItem(confirmBtn), true);
            }
            TradeService.refreshItem(TradeService.getOtherParty(player), slot);
        } else {
            event.setCancelled(true);
        }
    }
}

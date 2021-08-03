package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TradeUtils;
import com.ultikits.utils.EconomyUtils;
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

import java.util.Objects;

public class TradeListener implements Listener {

    @EventHandler
    public void onPlayerClickPlayer (PlayerInteractAtEntityEvent event) {
        if (!ConfigController.getConfig("trade").getBoolean("enable_shift_click_apply")) return;
        Player From = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            if (From.isSneaking()) {
                Player To = (Player) event.getRightClicked();
                if (TradeUtils.isPlayerInRequestMode(To) || TradeUtils.isPlayerInTradeMode(To)) {
                    From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_in_trading"));
                } else if (TradeUtils.isEnableTrade(To)) {
                    TradeUtils.requestTrade(From, To);
                } else {
                    From.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_disabled"));
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (TradeUtils.isPlayerInTradeMode(player)) {
            if (TradeUtils.isPlayerInTradeMode(player)) {
                TradeUtils.closeTrade(player, TradeUtils.getOtherParty(player));
            } else if (TradeUtils.isPlayerInRequestMode(player)) {
                if (TradeUtils.getInRequestMode().containsKey(player.getName())) {
                    TradeUtils.getInRequestMode().remove(player.getName());
                } else {
                    TradeUtils.getInRequestMode().inverse().remove(player.getName());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(UltiTools.languageUtils.getString("trade_title"))) {
            if (TradeUtils.isPlayerInTradeMode((Player) event.getPlayer())) {
                if (TradeUtils.getInTradeMode().containsKey(event.getPlayer().getName())) {
                    TradeUtils.closeTrade((Player) event.getPlayer(), TradeUtils.getOtherParty((Player) event.getPlayer()));
                } else {
                    TradeUtils.closeTrade(TradeUtils.getOtherParty((Player) event.getPlayer()), (Player) event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (TradeUtils.isPlayerInTradeMode(event.getEntity())) {
            if (TradeUtils.getInTradeMode().containsKey(event.getEntity().getName())) {
                TradeUtils.closeTrade(event.getEntity(), TradeUtils.getOtherParty(event.getEntity()));
            } else {
                TradeUtils.closeTrade(TradeUtils.getOtherParty(event.getEntity()), event.getEntity());
            }
        }
    }

    @EventHandler
    public void onPlayerClickItem (InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(UltiTools.languageUtils.getString("trade_title"))) {
            Inventory inventory = event.getClickedInventory();
            if (inventory == null) return;
            Player player = (Player) event.getWhoClicked();
            ItemStack itemStack = event.getCurrentItem();
            int slot = event.getSlot();
            if (inventory.getHolder() == null) {
                if (TradeUtils.getItemPlacementArea(player).contains(slot)) {
                    if (TradeUtils.isAllConfirmed(player)) {
                        event.setCancelled(true);
                    } else {
                        if (TradeUtils.getTradeConfirm().contains(player.getName()) || TradeUtils.getTradeConfirm().contains(TradeUtils.getOtherParty(player).getName())) {
                            TradeUtils.refreshConfirmation(player, inventory, inventory.getItem(53), true);
                        }
                        TradeUtils.refreshItem(TradeUtils.getOtherParty(player), slot);
                    }
                } else {
                    event.setCancelled(true);
                }
                if (TradeUtils.getBannedPosition().contains(slot)) {
                    event.setCancelled(true);
                    if (TradeUtils.isAllConfirmed(player)) return;
                    switch (slot) {
                        case 45:
                            TradeUtils.refreshConfirmation(player, inventory, inventory.getItem(53), true);
                            if (TradeUtils.getInTradeMode().containsKey(player.getName())) {
                                TradeUtils.closeTrade(player, TradeUtils.getOtherParty(player));
                            } else {
                                TradeUtils.closeTrade(TradeUtils.getOtherParty(player), player);
                            }
                            break;
                        case 48:
                            TradeUtils.refreshConfirmation(player, inventory, inventory.getItem(53), true);
                            if (event.isLeftClick()) {
                                if (EconomyUtils.checkMoney(player) > TradeUtils.getTradeMoney().get(player.getName())) {
                                    if (event.isShiftClick()) {
                                        if (TradeUtils.getTradeMoney().get(player.getName()) == 0) {
                                            TradeUtils.getTradeMoney().put(player.getName(), 1);
                                        } else {
                                            TradeUtils.getTradeMoney().put(player.getName(), TradeUtils.getTradeMoney().get(player.getName()) * 10);
                                        }
                                    } else {
                                        TradeUtils.getTradeMoney().put(player.getName(), TradeUtils.getTradeMoney().get(player.getName()) + 1);
                                    }
                                }
                            }
                            if (event.isRightClick()) {
                                if (event.isShiftClick()) {
                                    if (TradeUtils.getTradeMoney().get(player.getName()) != 0) {
                                        TradeUtils.getTradeMoney().put(player.getName(), TradeUtils.getTradeMoney().get(player.getName()) / 10);
                                    }
                                } else if (TradeUtils.getTradeMoney().get(player.getName()) - 1 >= 0) {
                                    TradeUtils.getTradeMoney().put(player.getName(), TradeUtils.getTradeMoney().get(player.getName()) - 1);
                                }
                            }
                            TradeUtils.updateMoneyLore(player, Objects.requireNonNull(inventory), Objects.requireNonNull(itemStack));
                            TradeUtils.updateMoneyLore(
                                    TradeUtils.getOtherParty(player),
                                    Objects.requireNonNull(TradeUtils.getOtherParty(player)).getOpenInventory().getTopInventory(),
                                    Objects.requireNonNull(itemStack)
                            );
                            break;
                        case 50:
                            TradeUtils.refreshConfirmation(player, inventory, inventory.getItem(53), true);
                            if (event.isLeftClick()) {
                                if (player.getTotalExperience() >= TradeUtils.getTradeExp().get(player.getName())) {
                                    if (event.isShiftClick()) {
                                        if (TradeUtils.getTradeExp().get(player.getName()) == 0) {
                                            TradeUtils.getTradeExp().put(player.getName(), 1);
                                        } else {
                                            TradeUtils.getTradeExp().put(player.getName(), TradeUtils.getTradeExp().get(player.getName()) * 10);
                                        }
                                    } else {
                                        TradeUtils.getTradeExp().put(player.getName(), TradeUtils.getTradeExp().get(player.getName()) + 1);
                                    }
                                }
                            }
                            if (event.isRightClick()) {
                                if (event.isShiftClick()) {
                                    if (TradeUtils.getTradeExp().get(player.getName()) != 0) {
                                        TradeUtils.getTradeExp().put(player.getName(), TradeUtils.getTradeExp().get(player.getName()) / 10);
                                    }
                                } else if (TradeUtils.getTradeExp().get(player.getName()) - 1 >= 0) {
                                    TradeUtils.getTradeExp().put(player.getName(), TradeUtils.getTradeExp().get(player.getName()) - 1);
                                }
                            }
                            TradeUtils.updateExpLore(player, Objects.requireNonNull(inventory), Objects.requireNonNull(itemStack));
                            TradeUtils.updateExpLore(
                                    TradeUtils.getOtherParty(player),
                                    Objects.requireNonNull(TradeUtils.getOtherParty(player)).getOpenInventory().getTopInventory(),
                                    Objects.requireNonNull(itemStack)
                            );
                            break;
                        case 53:
                            TradeUtils.getTradeConfirm().add(player.getName());
                            TradeUtils.refreshConfirmation(player, inventory, itemStack, false);
                            if (TradeUtils.isAllConfirmed(player)) {
                                if (TradeUtils.getInTradeMode().containsKey(player.getName())) {
                                    TradeUtils.confirmTrade(player, TradeUtils.getOtherParty(player));
                                } else {
                                    TradeUtils.confirmTrade(TradeUtils.getOtherParty(player), player);
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
}

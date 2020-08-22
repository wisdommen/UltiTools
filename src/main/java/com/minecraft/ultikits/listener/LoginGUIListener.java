package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.enums.LoginRegisterEnum;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

import static com.minecraft.ultikits.utils.GUIUtils.inventoryMap;
import static com.minecraft.ultikits.utils.DatabasePlayerTools.*;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class LoginGUIListener implements Listener {

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        Inventory currentInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), player.getName() + ".yml");

        ItemStack clicked = event.getCurrentItem();
        if (event.getView().getTitle().contains("登录界面")) {
            if (clicked != null) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains("点按输入数字")) {
                    int slot = currentInventory.firstEmpty();
                    if (slot >= 0 && slot < 9) {
                        currentInventory.setItem(slot, clicked);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 10, 1);
                    } else {
                        player.sendMessage(warning("不可以超过9个数字！"));
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("确认")) {
                    String password = "null";
                    try {
                        password = getThePassword(currentInventory);
                    }catch (Exception ignored){
                    }
                    if (!password.equals(getPlayerPassword(player))) {
                        player.sendMessage(warning("密码错误！"));
                    } else {
                        setIsLogin(player, true);
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "登录成功！");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
                        player.setGameMode(GameMode.SURVIVAL);
                        if (player.isFlying()){
                            player.setFlying(false);
                        }
                        player.closeInventory();
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("清空")) {
                    clearTheFirstLine(currentInventory);
                } else if (clicked.getItemMeta().getDisplayName().contains("退出")) {
                    player.kickPlayer(ChatColor.AQUA + "下次再见！");
                }
            }
        } else if (event.getView().getTitle().contains("注册界面")) {
            if (clicked != null) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains("点按输入数字")) {
                    int slot = currentInventory.firstEmpty();
                    if (slot < 9) {
                        currentInventory.setItem(slot, clicked);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 10, 1);
                    } else {
                        player.sendMessage(warning("不可以超过9个数字！"));
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("确认")) {
                    String password = "null";
                    try {
                        password = getThePassword(currentInventory);
                    }catch (Exception ignored){
                    }
                    if (getPlayerPassword(player) == null || getPlayerPassword(player).equals("")) {
                        setPlayerPassword(player, password);
                        clearTheFirstLine(currentInventory);
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "请再次输入密码！");
                    } else {
                        if (!password.equals(getPlayerPassword(player))) {
                            player.sendMessage(warning("两次输入的密码不同！请重试！"));
                            clearTheFirstLine(currentInventory);
                            file.delete();
                        } else {
                            setIsLogin(player, true);
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "注册成功！");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
                            player.setGameMode(GameMode.SURVIVAL);
                            if (player.isFlying()){
                                player.setFlying(false);
                            }
                            player.closeInventory();
                        }
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("清空")) {
                    clearTheFirstLine(currentInventory);
                } else if (clicked.getItemMeta().getDisplayName().contains("退出")) {
                    player.kickPlayer(ChatColor.AQUA + "下次再见！");
                    if (!getThePassword(currentInventory).equals("")){
                        setPlayerPassword(player, "");
                    }
                    if (file.exists()){
                        file.delete();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClose(InventoryCloseEvent event) {
        if (!getIsLogin((Player) event.getPlayer())) {
            Player player = (Player) event.getPlayer();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (event.getView().getTitle().contains("登录界面")) {
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.LOGIN).getInventory());
                    }else if (event.getView().getTitle().contains("注册界面")){
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.REGISTER).getInventory());
                    }
                }
            }.runTaskLater(UltiTools.getInstance(), 0L);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (getIsLogin(player)) {
            setIsLogin(player, false);
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (!getIsLogin(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (!getIsLogin(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if (!getIsLogin(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        if (!getIsLogin(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        if (!getIsLogin(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerOpenInventory(InventoryClickEvent event){
        if (!(event.getWhoClicked() instanceof Player)){
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!getIsLogin(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractInventory(InventoryInteractEvent event){
        if (!(event.getWhoClicked() instanceof Player)){
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!getIsLogin(player)){
            event.setCancelled(true);
        }
    }

    public void clearTheFirstLine(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, null);
        }
    }

    public String getThePassword(Inventory inventory) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack!=null) {
                stringBuilder.append(itemStack.getAmount());
            }
        }
        return stringBuilder.toString();
    }
}

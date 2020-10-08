package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.beans.CheckResponse;
import com.minecraft.ultikits.commands.LoginRegisterCommands;
import com.minecraft.ultikits.enums.LoginRegisterEnum;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.enums.Sounds;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.MD5Utils;
import com.minecraft.ultikits.utils.SendEmailUtils;
import com.minecraft.ultikits.utils.database.DatabasePlayerTools;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.minecraft.ultikits.utils.GUIUtils.*;
import static com.minecraft.ultikits.utils.MessagesUtils.info;
import static com.minecraft.ultikits.utils.database.DatabasePlayerTools.*;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class LoginGUIListener implements Listener {

    public static Map<UUID, Boolean> playerIsValidating = new HashMap<>();
    public static Map<UUID, Boolean> isRegisteringNewPassword = new HashMap<>();
    private static Map<UUID, String> tempPlayerPassword = new HashMap<>();

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
                        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_HAT), 10, 1);
                    } else {
                        player.sendMessage(warning("不可以超过9个数字！"));
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("确认")) {
                    String password = "null";
                    try {
                        password = getThePassword(currentInventory);
                    } catch (Exception ignored) {
                    }
                    if (!validateThePassword(player, password)) {
                        clearTheFirstLine(currentInventory);
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("清空")) {
                    clearTheFirstLine(currentInventory);
                } else if (clicked.getItemMeta().getDisplayName().contains("退出")) {
                    setIsLogin(player, true);
                    player.closeInventory();
                    player.kickPlayer(ChatColor.AQUA + "下次再见！");
                } else if (clicked.getItemMeta().getDisplayName().contains("忘记密码")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    if (!config.getBoolean("registered")) {
                        player.sendMessage(warning("你还没有绑定邮箱，不可以找回密码！"));
                        return;
                    }
                    player.sendMessage(info("正在发送验证码..."));
                    playerIsValidating.put(player.getUniqueId(), true);
                    setupValidationCodeLayout(player);
                    player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.VALIDATION.toString()).getInventory());

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            String email = DatabasePlayerTools.getPlayerEmail(player);
                            String code = getValidateCode();
                            CheckResponse response = SendEmailUtils.sendEmail(email, "服务器验证码", String.format("你正在找回我的世界服务器账户，你的验证码是 %s，请勿将验证码告诉他人。", code));
                            if (response.code.equals("200")) {
                                LoginRegisterCommands.sentCodePlayers.put(player.getUniqueId(), true);
                                LoginRegisterCommands.playersValidateCode.put(player.getUniqueId(), code);
                                player.sendMessage(info("验证码已经发送至 " + email + " ，若未收到请稍等。"));
                            } else {
                                player.sendMessage(warning("验证码发送失败，错误信息： " + response.msg));
                                playerIsValidating.put(player.getUniqueId(), false);
                            }
                        }
                    }.runTaskAsynchronously(UltiTools.getInstance());
                }
            }
        } else if (event.getView().getTitle().contains("注册界面")) {
            if (clicked != null) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains("点按输入数字")) {
                    int slot = currentInventory.firstEmpty();
                    if (slot < 9) {
                        currentInventory.setItem(slot, clicked);
                        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_HAT), 10, 1);
                    } else {
                        player.sendMessage(warning("不可以超过9个数字！"));
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("确认")) {
                    String password = "null";
                    try {
                        password = getThePassword(currentInventory);
                    } catch (Exception ignored) {
                    }
                    if (tempPlayerPassword.get(player.getUniqueId()) == null) {
                        tempPlayerPassword.put(player.getUniqueId(), password);
                        clearTheFirstLine(currentInventory);
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "请再次输入密码！");
                    } else {
                        if (!(password.equals(tempPlayerPassword.get(player.getUniqueId())))) {
                            player.sendMessage(warning("两次输入的密码不同！请重试！"));
                            clearTheFirstLine(currentInventory);
                        } else {
                            setIsLogin(player, true);
                            setPlayerPassword(player, password);
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "注册成功！");
                            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_BELL), 10, 1);
                            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_CHIME), 10, 1);
                            player.setGameMode(GameMode.SURVIVAL);
                            if (player.isFlying()) {
                                player.setFlying(false);
                            }
                            player.closeInventory();
                        }
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("清空")) {
                    clearTheFirstLine(currentInventory);
                } else if (clicked.getItemMeta().getDisplayName().contains("退出")) {
                    if(tempPlayerPassword.get(player.getUniqueId())!=null){
                        tempPlayerPassword.remove(player.getUniqueId());
                    }
                    if (isRegisteringNewPassword.get(player.getUniqueId())!=null &&isRegisteringNewPassword.get(player.getUniqueId())) {
                        isRegisteringNewPassword.put(player.getUniqueId(), false);
                        return;
                    }
                    setIsLogin(player, true);
                    player.closeInventory();
                    player.kickPlayer(ChatColor.AQUA + "下次再见！");
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
                    boolean isValidating;
                    if (playerIsValidating.get(player.getUniqueId())==null){
                        isValidating = false;
                    }else {
                        isValidating = playerIsValidating.get(player.getUniqueId());
                    }
                    if (event.getView().getTitle().contains("登录界面") && !isValidating) {
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.LOGIN).getInventory());
                    } else if (event.getView().getTitle().contains("注册界面") && !isValidating) {
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.REGISTER).getInventory());
                    } else if (event.getView().getTitle().contains("验证界面") && isValidating) {
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.VALIDATION).getInventory());
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
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!getIsLogin(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!getIsLogin(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!getIsLogin(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!getIsLogin(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!getIsLogin(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerOpenInventory(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!getIsLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractInventory(InventoryInteractEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!getIsLogin(player)) {
            event.setCancelled(true);
        }
    }

    private void clearTheFirstLine(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, null);
        }
    }

    private String getThePassword(Inventory inventory) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                stringBuilder.append(itemStack.getAmount());
            }
        }
        return stringBuilder.toString();
    }

    private void encryptExistPassword(Player player, String password) {
        if (getPlayerPassword(player).split("").length < 10) {
            setPlayerPassword(player, password);
        }
    }

    private boolean validateThePassword(Player player, String password) {
        encryptExistPassword(player, getPlayerPassword(player));
        password = MD5Utils.encrypt(password, player.getName());
        if (!password.equals(getPlayerPassword(player))) {
            player.sendMessage(warning("密码错误！"));
            return false;
        } else {
            setIsLogin(player, true);
            player.sendMessage(ChatColor.LIGHT_PURPLE + "登录成功！");
            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_BELL), 10, 1);
            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_CHIME), 10, 1);
            player.setGameMode(GameMode.SURVIVAL);
            if (player.isFlying()) {
                player.setFlying(false);
            }
            player.closeInventory();
            return true;
        }
    }

    private static String getValidateCode() {
        Random rand = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomNum = rand.nextInt(9) + 1;
            stringBuilder.append(randomNum);
        }
        return stringBuilder.toString();
    }
}

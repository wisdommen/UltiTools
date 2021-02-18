package com.ultikits.ultitools.listener;

import com.ultikits.beans.CheckResponse;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.commands.LoginRegisterCommands;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.enums.LoginRegisterEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DatabasePlayerTools;
import com.ultikits.utils.MD5Utils;
import com.ultikits.utils.MessagesUtils;
import com.ultikits.utils.SendEmailUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
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

import static com.ultikits.ultitools.utils.GUIUtils.*;
import static com.ultikits.ultitools.utils.DatabasePlayerTools.*;
import static com.ultikits.utils.MessagesUtils.info;
import static com.ultikits.utils.MessagesUtils.warning;


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

        if (event.getView().getTitle().equals(UltiTools.languageUtils.getString("login_login_page_title"))) {
            if (clicked != null) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("login_keyboard_button_label"))) {
                    int slot = currentInventory.firstEmpty();
                    if (slot >= 0 && slot < 9) {
                        currentInventory.setItem(slot, clicked);
                        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_HAT), 10, 1);
                    } else {
                        player.sendMessage(warning(UltiTools.languageUtils.getString("login_number_limit_warning")));
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_ok"))) {
                    String password = "null";
                    try {
                        password = getThePassword(currentInventory);
                    } catch (Exception ignored) {
                    }
                    if (!validateThePassword(player, password)) {
                        clearTheFirstLine(currentInventory);
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_clear"))) {
                    clearTheFirstLine(currentInventory);
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_quit"))) {
                    setIsLogin(player, true);
                    player.closeInventory();
                    player.kickPlayer(ChatColor.AQUA + UltiTools.languageUtils.getString("login_kick_message"));
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_forget_password"))) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    if (!config.getBoolean("registered")) {
                        player.sendMessage(warning(UltiTools.languageUtils.getString("emailregister_not_register_email")));
                        return;
                    }
                    player.sendMessage(info(UltiTools.languageUtils.getString("emailregister_sending_code")));
                    playerIsValidating.put(player.getUniqueId(), true);
                    setupValidationCodeLayout(player);
                    player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.VALIDATION.toString()).getInventory());

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            String email = DatabasePlayerTools.getPlayerEmail(player);
                            String code = getValidateCode();
                            CheckResponse response = SendEmailUtils.sendEmail(email, UltiTools.languageUtils.getString("emailregister_email_title"), String.format(UltiTools.languageUtils.getString("emialregister_forget_password_email_content"), code));
                            if (response.code.equals("200")) {
                                LoginRegisterCommands.sentCodePlayers.put(player.getUniqueId(), true);
                                LoginRegisterCommands.playersValidateCode.put(player.getUniqueId(), code);
                                player.sendMessage(info(String.format(UltiTools.languageUtils.getString("emailregister_code_sent"), email)));
                            } else {
                                player.sendMessage(warning(UltiTools.languageUtils.getString("emailregister_email_send_failed") + response.msg));
                                playerIsValidating.put(player.getUniqueId(), false);
                            }
                        }
                    }.runTaskAsynchronously(UltiTools.getInstance());
                }
            }
        } else if (event.getView().getTitle().equals(UltiTools.languageUtils.getString("login_register_page_tile"))) {
            if (clicked != null) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("login_keyboard_button_label"))) {
                    int slot = currentInventory.firstEmpty();
                    if (slot < 9 && slot >= 0) {
                        currentInventory.setItem(slot, clicked);
                        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_HAT), 10, 1);
                    } else {
                        player.sendMessage(warning(UltiTools.languageUtils.getString("login_number_limit_warning")));
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_ok"))) {
                    String password = "null";
                    try {
                        password = getThePassword(currentInventory);
                    } catch (Exception ignored) {
                    }
                    if (tempPlayerPassword.get(player.getUniqueId()) == null) {
                        tempPlayerPassword.put(player.getUniqueId(), password);
                        clearTheFirstLine(currentInventory);
                        player.sendMessage(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("login_register_enter_password_again"));
                    } else {
                        if (!(password.equals(tempPlayerPassword.get(player.getUniqueId())))) {
                            player.sendMessage(warning(UltiTools.languageUtils.getString("login_register_different_entered_password")));
                            clearTheFirstLine(currentInventory);
                        } else {
                            setIsLogin(player, true);
                            setPlayerPassword(player, password);
                            player.sendMessage(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("login_register_successfully"));
                            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_BELL), 10, 1);
                            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_CHIME), 10, 1);
                            player.setGameMode(GameMode.SURVIVAL);
                            if (player.isFlying()) {
                                player.setFlying(false);
                            }
                            player.closeInventory();
                        }
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_clear"))) {
                    clearTheFirstLine(currentInventory);
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_quit"))) {
                    if(tempPlayerPassword.get(player.getUniqueId())!=null){
                        tempPlayerPassword.remove(player.getUniqueId());
                    }
                    if (isRegisteringNewPassword.get(player.getUniqueId())!=null &&isRegisteringNewPassword.get(player.getUniqueId())) {
                        isRegisteringNewPassword.put(player.getUniqueId(), false);
                        return;
                    }
                    setIsLogin(player, true);
                    player.closeInventory();
                    player.kickPlayer(ChatColor.AQUA + UltiTools.languageUtils.getString("login_kick_message"));
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
                    if (event.getView().getTitle().equals(UltiTools.languageUtils.getString("login_login_page_title")) && !isValidating) {
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.LOGIN).getInventory());
                    } else if (event.getView().getTitle().equals(UltiTools.languageUtils.getString("login_register_page_tile")) && !isValidating) {
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.REGISTER).getInventory());
                    } else if (event.getView().getTitle().equals(UltiTools.languageUtils.getString("login_validation_page_title")) && isValidating) {
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
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (!getIsLogin(player)) {
            if (event.getFrom() != event.getTo()){
                event.setCancelled(true);
            }
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
        if (DatabasePlayerTools.getPlayerPassword(player).split("").length < 10) {
            setPlayerPassword(player, password);
        }
    }

    private boolean validateThePassword(Player player, String password) {
        encryptExistPassword(player, getPlayerPassword(player));
        password = MD5Utils.encrypt(password, player.getName());
        if (!password.equals(getPlayerPassword(player))) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("login_wrong_password")));
            return false;
        } else {
            setIsLogin(player, true);
            player.sendMessage(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("login_successfully"));
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

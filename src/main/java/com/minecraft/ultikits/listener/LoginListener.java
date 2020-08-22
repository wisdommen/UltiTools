package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.enums.LoginRegisterEnum;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.minecraft.ultikits.utils.GUIUtils.inventoryMap;
import static com.minecraft.ultikits.utils.GUIUtils.setupLoginRegisterLayout;
import static com.minecraft.ultikits.utils.DatabasePlayerTools.getIsLogin;
import static com.minecraft.ultikits.utils.DatabasePlayerTools.isPlayerAccountExist;

public class LoginListener implements Listener {

    public static GameMode gameMode;
    public static Map<String, Boolean> playerLoginStatus = new HashMap<>();

    static {
        File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), "loginState.yml");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Player player : Bukkit.getOnlinePlayers()){
                playerLoginStatus.put(player.getName(), true);
            }
        }else {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String each : config.getKeys(false)) {
                playerLoginStatus.put(each, config.getBoolean(each));
            }
        }
    }

    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        gameMode = player.getGameMode();
        if (!getIsLogin(player)) {
            playerLoginStatus.put(player.getName(), false);
            player.setGameMode(GameMode.CREATIVE);
            long delay = 1L;
            if (Bukkit.getPluginManager().getPlugin("SkinsRestorer") != null) delay = 60L;

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatColor.RED+"请输入你的登录信息！");
                    if (isPlayerAccountExist(player)) {
                        setupLoginRegisterLayout(player, LoginRegisterEnum.LOGIN);
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.LOGIN.toString()).getInventory());
                    } else {
                        setupLoginRegisterLayout(player, LoginRegisterEnum.REGISTER);
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.REGISTER.toString()).getInventory());
                    }
                }
            }.runTaskLater(UltiTools.getInstance(), delay);
        }
    }

    public static void checkPlayerAlreadyLogin(){
        for (Player player : Bukkit.getOnlinePlayers()){
            playerLoginStatus.put(player.getName(), true);
        }
    }

    public static void savePlayerLoginStatus(){
        File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), "loginState.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String each : playerLoginStatus.keySet()){
            config.set(each, false);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

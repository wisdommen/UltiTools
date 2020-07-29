package com.minecraft.ultikits.login;

import com.minecraft.ultikits.GUIs.LoginRegisterEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.minecraft.ultikits.GUIs.GUISetup.inventoryMap;
import static com.minecraft.ultikits.GUIs.GUISetup.setupLoginRegisterLayout;
import static com.minecraft.ultikits.utils.DatabasePlayerTools.isPlayerAccountExist;

public class LoginListener implements Listener {

    public static GameMode gameMode;
    public static Map<String, Boolean> playerLoginStatus = new HashMap<>();

    static {
        File file = new File(UltiTools.getInstance().getDataFolder()+"/loginData", "loginState.yml");
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
        playerLoginStatus.put(player.getName(), false);

        if (isPlayerAccountExist(player)) {
            setupLoginRegisterLayout(player, LoginRegisterEnum.LOGIN);
            player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.LOGIN.toString()).getInventory());
        } else {
            setupLoginRegisterLayout(player, LoginRegisterEnum.REGISTER);
            player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.REGISTER.toString()).getInventory());
        }
        player.setGameMode(GameMode.SPECTATOR);
    }

    public static void savePlayerLoginStatus(){
        File file = new File(UltiTools.getInstance().getDataFolder()+"/loginData", "loginState.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String each : playerLoginStatus.keySet()){
            config.set(each, playerLoginStatus.get(each));
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

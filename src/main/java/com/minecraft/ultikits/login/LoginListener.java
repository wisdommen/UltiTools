package com.minecraft.ultikits.login;

import com.minecraft.ultikits.GUIs.LoginRegisterEnum;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.minecraft.ultikits.GUIs.GUISetup.inventoryMap;
import static com.minecraft.ultikits.GUIs.GUISetup.setupLoginRegisterLayout;
import static com.minecraft.ultikits.utils.DatabasePlayerTools.isPlayerAccountExist;

public class LoginListener implements Listener {

    public static GameMode gameMode;

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        gameMode = player.getGameMode();

        if (isPlayerAccountExist(player)){
            setupLoginRegisterLayout(player, LoginRegisterEnum.LOGIN);
            player.openInventory(inventoryMap.get(player.getName()+LoginRegisterEnum.LOGIN.toString()).getInventory());
        }else {
            setupLoginRegisterLayout(player, LoginRegisterEnum.REGISTER);
            player.openInventory(inventoryMap.get(player.getName()+LoginRegisterEnum.REGISTER.toString()).getInventory());
        }
        player.setGameMode(GameMode.SPECTATOR);
    }
}

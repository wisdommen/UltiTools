package com.minecraft.ultikits.login;

import com.minecraft.ultikits.GUIs.LoginRegisterEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.minecraft.ultikits.GUIs.GUISetup.inventoryMap;
import static com.minecraft.ultikits.utils.DatabasePlayerTools.getIsLogin;
import static com.minecraft.ultikits.utils.DatabasePlayerTools.isPlayerAccountExist;

public class CheckGUIOpenTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!getIsLogin(player) && !player.getOpenInventory().getTitle().contains("界面")) {
                try {
                    if (isPlayerAccountExist(player)) {
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.LOGIN).getInventory());
                    } else {
                        player.openInventory(inventoryMap.get(player.getName() + LoginRegisterEnum.REGISTER).getInventory());
                    }
                }catch (NullPointerException e){
                    player.kickPlayer(ChatColor.AQUA+"腐竹重载了插件，请重新登陆！");
                }
            }
        }
    }
}

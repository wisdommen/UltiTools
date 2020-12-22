package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.views.CreateKitsView;
import com.ultikits.ultitools.views.KitsView;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;


public class KitsCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        switch (strings.length){
            case 0:
                Inventory inventory = KitsView.setUp(player);
                player.openInventory(inventory);
                return true;
//            case 1:
//                if (player.hasPermission("ultikits.tools.admin")){
//                    if (ConfigController.getString(""))
//                    Inventory inventory1 = CreateKitsView.setUp()
//                }
            default:
                return false;
        }
    }
}

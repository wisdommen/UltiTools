package com.minecraft.ultikits.remoteChest;

import com.minecraft.ultikits.GUIs.GUISetup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoteBagCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0 && "bag".equals(command.getName())) {
                GUISetup.setPlayerRemoteChest(player);
                player.openInventory(GUISetup.inventoryMap.get("chest").getInventory());
                return true;
            }
        }
        return false;
    }
}

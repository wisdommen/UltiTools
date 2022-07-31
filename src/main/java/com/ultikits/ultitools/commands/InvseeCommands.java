package com.ultikits.ultitools.commands;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.views.ArmorView;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.ultikits.enums.Sounds.BLOCK_NOTE_BLOCK_CHIME;


public class InvseeCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("command_can_only_perform_in_game")));
            return false;
        }
        if(sender.hasPermission("ultikits.tools.admin")) {
            if(strings.length != 1) {
                sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("invalid_arguments")));
                return false;
            } else {  //命令正确使用
                String targetPlayerName = strings[0];
                Player player = (Player)sender;
                Player targetPlayer = Bukkit.getServer().getPlayer(targetPlayerName);
                switch (command.getName()) {
                    case "invsee":
                        if(targetPlayer == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("player_doesnt_exist_or_offline")));
                            return true;
                        } else {
                            player.openInventory(targetPlayer.getInventory());
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
                            player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("invsee_success") + targetPlayerName));
                            return true;
                        }

                    case "endersee":
                        if(targetPlayer == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("player_doesnt_exist_or_offline")));
                            return true;
                        } else {
                            player.openInventory(targetPlayer.getEnderChest());
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
                            player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("enderChest_see_success") + targetPlayerName));
                            return true;
                        }
                    case "armorsee":
                        if(targetPlayer == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("player_doesnt_exist_or_offline")));
                        } else {
                            Inventory inventory = ArmorView.setUp(targetPlayer);
                            player.openInventory(inventory);
                            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(BLOCK_NOTE_BLOCK_CHIME), 10, 1);
                            player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("armor_see_success") + targetPlayerName));
                        }
                        return true;
                    default:
                        return false;
                }
            }
        } else {
            sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
            return true;
        }
    }
}

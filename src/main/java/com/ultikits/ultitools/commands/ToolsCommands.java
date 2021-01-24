package com.ultikits.ultitools.commands;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.CustomGuiUtils;
import com.ultikits.ultitools.utils.FunctionUtils;
import com.ultikits.ultitools.views.CustomGUIView;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.ultikits.utils.MessagesUtils.warning;


public class ToolsCommands implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if (!(command.getName().equalsIgnoreCase("ultitools"))) {
            return false;
        }
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (!(player.isOp() || player.hasPermission("ultitools.tools.commands"))) {
                sender.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
                return false;
            }
        }
        switch (strings.length){
            case 1:
                switch (strings[0]){
                    case "reload":
                        ConfigController.reloadAll();
                        sender.sendMessage(warning(UltiTools.languageUtils.getString("config_reloaded")));
                        return true;
                    default:
                        if (!UltiTools.isProVersion){
                            return false;
                        }
                        if (player == null) {
                            return false;
                        }
                        String signature = CustomGuiUtils.getSignature(strings[0]);
                        if (signature == null) {
                            return false;
                        }
                        Inventory customGui = CustomGUIView.setUp(signature, player);
                        player.openInventory(customGui);
                        return true;
                }
            case 2:
                switch (strings[0]){
                    case "enable":
                        FunctionUtils.functionSwitch(strings[1], true);
                        return true;
                    case "disable":
                        FunctionUtils.functionSwitch(strings[1], false);
                        return true;
                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> tabCommands = new ArrayList<>();
        if (!sender.hasPermission("ultikits.tools.commands")) {
            return null;
        }
        switch (args.length){
            case 1:
                tabCommands.add("reload");
                tabCommands.add("disable");
                tabCommands.add("enable");
                return tabCommands;
            case 2:
                switch (args[0]){
                    case "disable":
                    case "enable":
                        tabCommands.addAll(FunctionUtils.getAllFunctions());
                        return tabCommands;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }
}

package com.ultikits.ultitools.commands;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.listener.CustomGUIListener;
import com.ultikits.ultitools.listener.RightClickListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.CustomGuiUtils;
import com.ultikits.ultitools.utils.FunctionUtils;
import com.ultikits.ultitools.views.CustomGUIView;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.ultikits.utils.MessagesUtils.warning;
import static org.bukkit.Bukkit.getServer;


public class ToolsCommands implements TabExecutor {
    List<String> listeners = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if (!(command.getName().equalsIgnoreCase("ultitools"))) {
            return false;
        }
        switch (strings.length){
            case 1:
                switch (strings[0]){
                    case "reload":
                        if (!sender.hasPermission("ultitools.tools.commands")){
                            sender.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
                            return false;
                        }
                        ConfigController.reloadAll();
                        sender.sendMessage(warning(UltiTools.languageUtils.getString("config_reloaded")));
                        return true;
                    default:
                        if (!UltiTools.getInstance().getProChecker().getProStatus()){
                            return false;
                        }
                        if (!(sender instanceof Player)) {
                            return false;
                        }
                        Player player = (Player) sender;
                        String signature = CustomGuiUtils.getSignature(strings[0]);
                        if (signature == null) {
                            return false;
                        }
                        Inventory customGui = CustomGUIView.setUp(signature, player);
                        CustomGUIListener listener = new CustomGUIListener(signature);
                        if (!listeners.contains(listener.getSignature())){
                            //菜单绑定物品lore
                            Bukkit.getServer().getPluginManager().registerEvents(new RightClickListener(),UltiTools.getInstance());
                            getServer().getPluginManager().registerEvents(new CustomGUIListener(signature), UltiTools.getInstance());
                            listeners.add(listener.getSignature());
                        }
                        player.openInventory(customGui);
                        return true;
                }
            case 2:
                if (!sender.hasPermission("ultitools.tools.commands")){
                    sender.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
                    return false;
                }
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

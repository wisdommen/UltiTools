package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.GUIUtils;
import com.minecraft.ultikits.views.RemoteBagView;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.minecraft.ultikits.listener.ChestPageListener.loadBag;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;
import static com.minecraft.ultikits.utils.Utils.getFiles;

public class RemoteBagCommands extends AbstractTabExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if ("bag".equals(command.getName())) {
            switch (strings.length) {
                case 0:
                    Inventory bag = RemoteBagView.setUp(player.getName());
                    player.openInventory(bag);
                    return true;
                case 2:
                    if (!(player.hasPermission("ultitools.tools.admin") || player.hasPermission("ultikits.tools.admin"))) {
                        return false;
                    }
                    List<File> fileList = getFiles(ConfigsEnum.PLAYER_CHEST.toString());
                    if (fileList == null) {
                        player.sendMessage(warning(UltiTools.languageUtils.getWords("bag_no_user")));
                        return true;
                    }
                    for (File file : fileList) {
                        String playerName = file.getName().replace(".yml", "");
                        if (playerName.equals(strings[0])) {
                            YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(file);
                            int size = chestConfig.getKeys(false).size();
                            try {
                                if (size < Integer.parseInt(strings[1])) {
                                    player.sendMessage(warning(UltiTools.languageUtils.getWords("bag_player_does_not_have_this_bag")));
                                    return true;
                                }
                            } catch (NumberFormatException e) {
                                player.sendMessage(warning(UltiTools.languageUtils.getWords("bag_enter_number_of_the_bag")));
                                return true;
                            }
                            String bagNumber = strings[1];
                            String bagName = String.format(UltiTools.languageUtils.getWords("bag_title"), strings[0], bagNumber);
                            loadBag(bagName, player, Bukkit.getOfflinePlayer(strings[0]));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getWords("bag_player_data_not_exists")));
                    return true;
                case 3:
                    if (!UltiTools.isProVersion) {
                        return false;
                    }
                    switch (strings[1]) {
                        case "share":

                        case "unshare":
                            return true;
                        default:
                            return false;
                    }
                default:
                    return false;
            }

        }
        return false;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> tabs = new ArrayList<>();
        if (!(player.hasPermission("ultitools.tools.admin") || player.hasPermission("ultikits.tools.admin"))) {
            return null;
        }
        switch (strings.length) {
            case 1:
                List<File> fileList = getFiles(ConfigsEnum.PLAYER_CHEST.toString());
                if (fileList == null) return null;
                for (File file : fileList) {
                    tabs.add(file.getName().replace(".yml", ""));
                }
                return tabs;
            case 2:
                File file = new File(ConfigsEnum.PLAYER_CHEST.toString(), strings[0] + ".yml");
                YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(file);
                int size = chestConfig.getKeys(false).size();
                for (int i = 1; i <= size; i++) {
                    tabs.add(i + "");
                }
                return tabs;
        }
        return null;
    }
}

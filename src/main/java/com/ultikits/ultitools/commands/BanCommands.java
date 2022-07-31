package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TimeUtils;
import com.ultikits.ultitools.views.BanlistView;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @author Shpries
 */
@CmdExecutor(function = "ban", permission = "ultikits.tools.admin", description = "ban_function", alias = "ultiban,ultibanip,ultibanlist")
public class BanCommands extends AbstractPlayerCommandExecutor {
    private int banDays;
    private String from;
    private String to;
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!player.isOp()) {
            return false;
        }
        YamlConfiguration banListConfig = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.BANLIST.toString()));
        ConfigurationSection banListOfPlayerSection = banListConfig.getConfigurationSection("banlist.banned-players");
        if (strings.length == 3 && "ultiban".equals(command.getName())) {
            try {
                banDays = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("ban_invalid_number")));
                return false;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    String name = strings[0];
                    String uuid = getUUID(Bukkit.getPlayer(name));
                    if (banListOfPlayerSection == null || !banListOfPlayerSection.getKeys(false).contains(uuid)) {
                        if(banDays <= 0) {
                            from = new TimeUtils().getTimeWithDate();
                            to = "FOREVER";
                        } else {
                            String[] time = new TimeUtils().getTimeAndAdd(banDays);
                            from = time[0];
                            to = time[1];
                        }
                        String reason = strings[2];
                        banListConfig.set("banlist.banned-players." + uuid + ".id", name);
                        banListConfig.set("banlist.banned-players." + uuid + ".from", from);
                        banListConfig.set("banlist.banned-players." + uuid + ".to", to);
                        banListConfig.set("banlist.banned-players." + uuid + ".reason", reason);
                        try {
                            banListConfig.save(new File(ConfigsEnum.BANLIST.toString()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("ban_player_success")));
                    } else {
                        player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("ban_already_banned")));
                    }
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
            return true;
        }
        if (strings.length == 2 && "ultibanip".equals(command.getName())) {
            YamlConfiguration loginConfig = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.LOGIN.toString()));
            ConfigurationSection loginConfigIpSection = loginConfig.getConfigurationSection("ip");
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (String ip : loginConfigIpSection.getKeys(false)) {
                        if (loginConfig.getStringList("ip." + ip + ".players").contains(getUUID(Bukkit.getPlayer(strings[0])))) {
                            String reason = strings[1];
                            banListConfig.set("banlist.banned-ips", ip);
                            banListConfig.set("banlist.banned-ips." + ip,reason);
                            try {
                                banListConfig.save(new File(ConfigsEnum.BANLIST.toString()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("ban_ip_success")));
                            break;
                        }
                    }
                }
            }.runTaskAsynchronously(UltiTools.getInstance());

            return true;
        }
        if (strings.length == 0 && "ultibanlist".equals(command.getName())) {
            if(banListOfPlayerSection == null) {
                player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("ban_no_player_banned")));
                return true;
            }
            player.openInventory(BanlistView.setup());
            return true;
        }
        player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("wrong_format")));
        return false;
    }
    private String getUUID(Player p) {
        if(Bukkit.getServer().getOnlineMode()) {
            return p.getUniqueId().toString();
        } else {
            return java.util.UUID.nameUUIDFromBytes(String.format("OfflinePlayer:%s", p.getName()).getBytes(StandardCharsets.UTF_8)).toString();
        }
    }
}

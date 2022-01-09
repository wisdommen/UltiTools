package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.beans.CheckResponse;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.enums.PermissionsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DatabasePlayerTools;
import com.ultikits.ultitools.utils.Utils;
import com.ultikits.utils.SendEmailUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.ultikits.utils.MessagesUtils.info;
import static com.ultikits.utils.MessagesUtils.warning;

public class LoginRegisterCommands extends AbstractTabExecutor {
    public static Map<UUID, Boolean> sentCodePlayers = new HashMap<>();
    public static Map<UUID, String> playersValidateCode = new HashMap<>();
    public static Map<UUID, String> playersEmail = new HashMap<>();

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.getInstance().getConfig().getBoolean("enable_pro") || !UltiTools.getInstance().getProChecker().getProStatus()) {
            if (player.hasPermission(PermissionsEnum.ADMIN.getPermission())) {
                player.sendMessage(warning(UltiTools.languageUtils.getString("warning_pro_function")));
            } else {
                player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
            }
            return true;
        }
        File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        switch (strings.length){
            case 1:
                if (config.getBoolean("registered")) {
                    player.sendMessage(warning(UltiTools.languageUtils.getString("emailregister_registered")));
                    return true;
                }
                if (strings[0].contains("@")) {
                    player.sendMessage(info(UltiTools.languageUtils.getString("emailregister_sending_code")));
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            String email = strings[0];
                            String code = getValidateCode();
                            CheckResponse response = SendEmailUtils.sendEmail(email, UltiTools.languageUtils.getString("emailregister_email_title"), String.format(UltiTools.languageUtils.getString("emailregister_email_content"), code));
                            if (response.code.equals("200")) {
                                sentCodePlayers.put(player.getUniqueId(), true);
                                playersValidateCode.put(player.getUniqueId(), code);
                                playersEmail.put(player.getUniqueId(), strings[0]);
                                player.sendMessage(info(String.format(UltiTools.languageUtils.getString("emailregister_code_sent"), strings[0])));
                            } else {
                                player.sendMessage(warning(UltiTools.languageUtils.getString("emailregister_email_send_failed") + response.msg));
                            }
                        }
                    }.runTaskAsynchronously(UltiTools.getInstance());

                } else if (strings[0].length() == 6) {
                    if (!sentCodePlayers.get(player.getUniqueId())) {
                        player.sendMessage(warning(UltiTools.languageUtils.getString("emailregister_enter_email_first")));
                        return true;
                    }
                    String validateCode = strings[0];
                    if (playersValidateCode.get(player.getUniqueId()).equals(validateCode)) {
                        player.sendMessage(info(UltiTools.languageUtils.getString("emailregister_email_validated")));
                        config.set("registered", true);
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //绑定邮箱奖励
                        YamlConfiguration loginConfig = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.LOGIN.toString()));
                        if(loginConfig.getBoolean("enable_emailregister_reward")) {
                            if(loginConfig.getStringList("emailregister_reward_command") != null) {
                                List<String> rewardcmds = loginConfig.getStringList("emailregister_reward_commands");
                                for(String cmd : rewardcmds) {
                                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),cmd.replace("%player%",player.getName()));
                                }
                            }
                        }
                        DatabasePlayerTools.setPlayerEmail(player, playersEmail.get(player.getUniqueId()));
                        return true;
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("emailregister_code_invalid")));
                } else {
                    return false;
                }
            case 3:
                if (strings[0].equals("set")){
                    String playerName = strings[1];
                    if (!DatabasePlayerTools.isPlayerAccountExist(playerName)){
                        player.sendMessage(warning(UltiTools.languageUtils.getString("no_such_player")));
                        return true;
                    }
                    DatabasePlayerTools.setPlayerPassword(playerName, strings[2]);
                    player.sendMessage(info(UltiTools.languageUtils.getString("emailregister_password_changed")));
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> tabs = new ArrayList<>();
        if (!(player.hasPermission("ultitools.tools.admin") || player.hasPermission("ultikits.tools.admin"))) {
            return null;
        }
        switch (strings.length) {
            case 1:
                tabs.add("set");
                return tabs;
            case 2:
                List<File> fileList = Utils.getFiles(ConfigsEnum.PLAYER_LOGIN.toString());
                if (fileList == null) return null;
                for (File file : fileList) {
                    if (file.getName().equals("loginState.yml")) continue;
                    tabs.add(file.getName().replace(".yml", ""));
                }
                return tabs;
        }
        return null;
    }

    public static String getValidateCode() {
        Random rand = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomNum = rand.nextInt(10);
            stringBuilder.append(randomNum);
        }
        return stringBuilder.toString();
    }
}

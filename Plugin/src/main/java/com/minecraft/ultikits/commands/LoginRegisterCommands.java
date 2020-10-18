package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.beans.CheckResponse;
import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.enums.PermissionsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.SendEmailUtils;
import com.minecraft.ultikits.utils.database.DatabasePlayerTools;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.minecraft.ultikits.utils.MessagesUtils.info;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class LoginRegisterCommands extends AbstractTabExecutor {
    public static Map<UUID, Boolean> sentCodePlayers = new HashMap<>();
    public static Map<UUID, String> playersValidateCode = new HashMap<>();
    public static Map<UUID, String> playersEmail = new HashMap<>();

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.isProVersion) {
            if (player.hasPermission(PermissionsEnum.ADMIN.getPermission())) {
                player.sendMessage(warning(UltiTools.languageUtils.getWords("warning_pro_fuction")));
            } else {
                player.sendMessage(warning(UltiTools.languageUtils.getWords("no_permission")));
            }
            return true;
        }
        File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (config.getBoolean("registered")) {
            player.sendMessage(warning(UltiTools.languageUtils.getWords("emailregister_registered")));
            return true;
        }
        if (strings.length == 1) {
            if (strings[0].contains("@")) {
                player.sendMessage(info(UltiTools.languageUtils.getWords("emailregister_sending_code")));
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        String email = strings[0];
                        String code = getValidateCode();
                        CheckResponse response = SendEmailUtils.sendEmail(email, UltiTools.languageUtils.getWords("emailregister_email_title"), String.format(UltiTools.languageUtils.getWords("emailregister_email_content"), code));
                        if (response.code.equals("200")) {
                            sentCodePlayers.put(player.getUniqueId(), true);
                            playersValidateCode.put(player.getUniqueId(), code);
                            playersEmail.put(player.getUniqueId(), strings[0]);
                            player.sendMessage(info(String.format(UltiTools.languageUtils.getWords("emailregister_code_sent"), strings[0])));
                        } else {
                            player.sendMessage(warning(UltiTools.languageUtils.getWords("emailregister_email_send_failed") + response.msg));
                        }
                    }
                }.runTaskAsynchronously(UltiTools.getInstance());

            } else if (strings[0].length() == 6) {
                if (!sentCodePlayers.get(player.getUniqueId())) {
                    player.sendMessage(warning(UltiTools.languageUtils.getWords("emailregister_enter_email_first")));
                    return true;
                }
                String validateCode = strings[0];
                if (playersValidateCode.get(player.getUniqueId()).equals(validateCode)) {
                    player.sendMessage(info(UltiTools.languageUtils.getWords("emailregister_email_validated")));
                    config.set("registered", true);
                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DatabasePlayerTools.setPlayerEmail(player, playersEmail.get(player.getUniqueId()));
                    return true;
                }
                player.sendMessage(warning(UltiTools.languageUtils.getWords("emailregister_code_invalid")));
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
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

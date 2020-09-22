package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.beans.CheckResponse;
import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.enums.PermissionsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.SendEmailUtils;
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
    static Map<UUID, Boolean> sentCodePlayers = new HashMap<>();
    static Map<UUID, String> playersValidateCode = new HashMap<>();
    static Map<UUID, String> playersEmail = new HashMap<>();

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.isProVersion) {
            if (player.hasPermission(PermissionsEnum.ADMIN.getPermission())) {
                player.sendMessage(warning("这是一个付费版功能！"));
            } else {
                player.sendMessage(warning("你没有权限！"));
            }
            return true;
        }
        File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), player.getName()+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!config.getBoolean("registered")){
            player.sendMessage(warning("你已经验证过邮箱了！"));
            return true;
        }
        if (strings.length == 1) {
            if (strings[0].contains("@")) {
                if (sentCodePlayers.get(player.getUniqueId()) != null && sentCodePlayers.get(player.getUniqueId())) {
                    player.sendMessage(warning("验证码已经发送过了，请稍等片刻!"));
                    return true;
                }
                player.sendMessage(info("正在发送验证码..."));
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        String email = strings[0];
                        String code = getValidateCode();
                        CheckResponse response = SendEmailUtils.sendEmail(email, "服务器注册验证码", String.format("你正在注册我的世界服务器账户，你的验证码是 %s，请勿将验证码告诉他人。", code));
                        if (response.code.equals("200")) {
                            sentCodePlayers.put(player.getUniqueId(), true);
                            playersValidateCode.put(player.getUniqueId(), code);
                            playersEmail.put(player.getUniqueId(), strings[0]);
                            player.sendMessage(info("验证码已经发送至 " + strings[0] + " ，若未收到请稍等。"));
                        } else {
                            player.sendMessage(warning("验证码发送失败，错误信息： " + response.msg));
                        }
                    }
                }.runTaskAsynchronously(UltiTools.getInstance());

            } else if (strings[0].length() == 6) {
                if (!sentCodePlayers.get(player.getUniqueId())) {
                    player.sendMessage(warning("请先输入邮箱！"));
                    return true;
                }
                String validateCode = strings[0];
                if (playersValidateCode.get(player.getUniqueId()).equals(validateCode)) {
                    player.sendMessage(info("邮箱验证成功！"));
                    config.set("registered", true);
                    config.set("register_email", playersEmail.get(player.getUniqueId()));
                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                player.sendMessage(warning("验证码错误，请重新输入验证码！"));
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

    private static String getValidateCode() {
        Random rand = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomNum = rand.nextInt(10);
            stringBuilder.append(randomNum);
        }
        return stringBuilder.toString();
    }
}

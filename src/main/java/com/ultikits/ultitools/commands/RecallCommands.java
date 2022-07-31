package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.beans.CheckResponse;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import com.ultikits.utils.SendEmailUtils;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Objects;

import static com.ultikits.utils.MessagesUtils.info;
import static com.ultikits.utils.MessagesUtils.warning;

@CmdExecutor(function = "recall", permission = "ultikits.tools.email", description = "recall_function", alias = "email,ultimail,mail,mails")
public class RecallCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.getInstance().getConfig().getBoolean("enable_pro") || !UltiTools.getInstance().getProChecker().getProStatus()) {
            player.sendMessage(UltiTools.languageUtils.getString("warning_pro_function"));
            return true;
        }
        if(!player.isOp()) {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
            return false;
        }
        File loginData = new File(UltiTools.getInstance().getDataFolder() + File.separator + "loginData");
        int sum=0;
        int suc = 0;
        for(File f : Objects.requireNonNull(loginData.listFiles())) {
            YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(f);
            if(playerConfig.getString("register_email") != null) {
                sum++;
                String email = playerConfig.getString("register_email");
                player.sendMessage(String.format(UltiTools.languageUtils.getString("recall_sending_emails"),sum));
                CheckResponse response = SendEmailUtils.sendEmail(email, UltiTools.languageUtils.getString("recall_email_title"), UltiTools.languageUtils.getString("recall_email_content"));
                if (response.code.equals("200")) {
                    suc++;
                    player.sendMessage(info(String.format(UltiTools.languageUtils.getString("recall_email_send_success"),sum,email)));
                } else {
                    player.sendMessage(warning(String.format(UltiTools.languageUtils.getString("recall_email_send_fail"),sum,email)));
                }
            }
        }
        if(sum==0) {
            player.sendMessage(UltiTools.languageUtils.getString("recall_no_player_email"));
            return true;
        }
        player.sendMessage(String.format(UltiTools.languageUtils.getString("recall_conclusion"),sum,sum-suc));
        return true;
    }
}

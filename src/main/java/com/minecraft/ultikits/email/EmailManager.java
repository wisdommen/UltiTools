package com.minecraft.ultikits.email;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class EmailManager {

    private final File folder;
    private final File file;
    private final YamlConfiguration config;
    private final Integer count;
    private final Player player;

    public EmailManager(Player player) {
        folder = new File(UltiTools.getInstance().getDataFolder() + "/playerData");
        file = new File(folder, player.getName() + ".yml");

        this.player = player;

        if (!file.exists()) {
            folder.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            config = new YamlConfiguration();
        } else {
            config = YamlConfiguration.loadConfiguration(file);
        }

        count = config.getInt("count");
    }

    public Integer getEmailNum() {
        return count;
    }

    public File getFile() {
        return file;
    }

    public File getFolder() {
        return folder;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public StringBuilder getEmails() {
        StringBuilder emails = new StringBuilder();
        for (int i = 1; i <= config.getConfigurationSection("email").getKeys(false).size(); i++) {
            try {
                String mail = Objects.requireNonNull(config.getString("email." + i));
                emails.append("第").append(i).append("封邮件：\n");
                emails.append(mail).append("\n");
            } catch (NullPointerException e) {
                break;
            }
        }
        return emails;
    }

    public String getHistoryEmails() {
        if (config.getString("historyEmail") != null) {
            return Objects.requireNonNull(config.getString("historyEmail"));
        }
        return "你还没有收到过任何邮件！";
    }

    public void setHistoryEmail() {
        for (int a = 1; a <= config.getConfigurationSection("email").getKeys(false).size(); a++) {
            try {
                String Hmail = Objects.requireNonNull(config.getString("email." + a));
                if (config.getString("historyEmail") != null) {
                    config.set("historyEmail", config.getString("historyEmail") + Hmail + "\n");
                }
                if (config.getString("historyEmail") == null) {
                    config.set("historyEmail", config.getString("email." + a) + "\n");
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        config.set("email", null);
        config.set("count", 0);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean deleteHistoryEmails(){
        if (config.getString("historyEmail") != null) {
            config.set("historyEmail", null);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public Boolean sendEmail(Player target, String message){
        File f = new File(UltiTools.getInstance().getDataFolder() + "/playerData", target.getName() + ".yml");
        YamlConfiguration config2;
        if (!f.exists()) {
            return false;
        } else {
            config2 = YamlConfiguration.loadConfiguration(f);
            if (config2.getString("email.1") == null) {
                config2.set("email.1", "来自" + player.getName() + ":" + message);
                config2.set("count", 1);
                try {
                    config2.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                int x = config.getConfigurationSection("email").getKeys(false).size();
                config2.set("email." + (x + 1), "来自" + player.getName() + ":" + message);
                config2.set("count", config2.getInt("count") + 1);
                try {
                    config2.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Bukkit.getOnlinePlayers().contains(target)){
                    target.sendMessage(ChatColor.GOLD+"收到来自"+player.getName()+"的新邮件！");
                    target.sendMessage(ChatColor.GOLD+"输入 /email read 来查看！");
                }
            }
            return true;
        }
    }

}

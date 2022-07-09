package com.ultikits.ultitools.checker;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.utils.ExceptionUtils;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.YamlFileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PlayerlistChecker {
    File playerlistFile = new File(ConfigsEnum.PLAYERLIST.toString());
    YamlConfiguration playerlistConfig = YamlConfiguration.loadConfiguration(playerlistFile);

    public void playerlistNewChecker() {
        if (!playerlistFile.exists() || !playerlistConfig.getStringList("playerlist").isEmpty()) {
            if (Bukkit.getServer().getOnlineMode()) {
                onlineModeChanger();
            } else {
                offlineModeChanger();
            }
        }
    }

    private void offlineModeChanger() {
        List<String> playerlistInChecker = playerlistConfig.getStringList("playerlist");
        delAndCreate();
        for (String playerNameInChecker : playerlistInChecker) {
            String UUID = java.util.UUID.nameUUIDFromBytes(String.format("OfflinePlayer:%s", playerNameInChecker).getBytes(StandardCharsets.UTF_8)).toString();
            addPlayerlist(UUID,playerNameInChecker);
        }
    }

        private void onlineModeChanger () {
            List<String> playerlistInChecker = playerlistConfig.getStringList("playerlist");
            delAndCreate();
            for (String playerNameInChecker : playerlistInChecker) {
                String UUID = Bukkit.getPlayer(playerNameInChecker).getUniqueId().toString();
                addPlayerlist(UUID, playerNameInChecker);
            }
        }

        private void delAndCreate () {
            playerlistFile.delete();
            new YamlFileUtils().saveYamlFile(UltiTools.getInstance().getDataFolder().getPath() + File.separator + "playerData" + File.separator + "playerlist", "playerlist.yml", "playerlist.yml");
        }

        private void addPlayerlist (String UUID, String playerName) {
            playerlistConfig.set("playerlist." + UUID, playerName);
            saveConfig();
        }

        private void saveConfig () {
            try {
                playerlistConfig.save(playerlistFile);
            } catch (IOException e) {
                ExceptionUtils.catchException(e);
            }
        }
    }

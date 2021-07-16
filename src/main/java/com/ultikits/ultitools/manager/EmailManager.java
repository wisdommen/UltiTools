package com.ultikits.ultitools.manager;

import com.ultikits.beans.EmailContentBean;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EmailManager {

    private final File file;
    private final String playerName;

    public EmailManager(@NotNull File playerFile) {
        file = playerFile;
        playerName = playerFile.getName().replace(".yml", "");
    }

    public File getFile() {
        return file;
    }

    public Map<String, EmailContentBean> getEmails() {
        Map<String, EmailContentBean> emails = new HashMap<>();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String uuid : config.getKeys(false)) {
            if (config.getConfigurationSection(uuid).getKeys(false).contains("item")) {
                ItemStack itemStack = setupItemStack(uuid);
                emails.put(uuid, new EmailContentBean(uuid, config.getString(uuid + ".sender"), config.getString(uuid + ".message"), itemStack, config.getBoolean(uuid + ".isRead"), config.getBoolean(uuid + ".isClaimed")));
            } else {
                emails.put(uuid, new EmailContentBean(uuid, config.getString(uuid + ".sender"), config.getString(uuid + ".message"), config.getBoolean(uuid + ".isRead")));
            }
        }
        return emails;
    }


    /**
     * @param receiverFile 发送给某个人
     * @param message      所要发送的消息
     * @param itemStack    发送包含的物品
     * @return 是否发送成功
     */
    public Boolean sendTo(@NotNull File receiverFile, String message, ItemStack itemStack) {
        if (receiverFile.exists()) {
            EmailManager emailManager = new EmailManager(receiverFile);
            EmailContentBean emailContentManager = new EmailContentBean(generateUUID(), playerName, message, itemStack, false, false);
            emailManager.saveEmail(emailContentManager.getUuid(), emailContentManager.getSender(), emailContentManager.getMessage(), emailContentManager.getItemStack());
            return true;
        }
        return false;
    }

    public Boolean sendTo(@NotNull File receiverFile, String message, ItemStack itemStack, List<String> commands) {
        if (receiverFile.exists()) {
            EmailManager emailManager = new EmailManager(receiverFile);
            EmailContentBean emailContentManager = new EmailContentBean(generateUUID(), playerName, message, commands, itemStack, false, false);
            emailManager.saveEmail(emailContentManager.getUuid(), emailContentManager.getSender(), emailContentManager.getMessage(), emailContentManager.getCommands(), emailContentManager.getItemStack());
            return true;
        }
        return false;
    }

    public Boolean sendTo(@NotNull File receiverFile, String message) {
        if (receiverFile.exists()) {
            EmailManager emailManager = new EmailManager(receiverFile);
            EmailContentBean emailContentManager = new EmailContentBean(generateUUID(), playerName, message, false);
            emailManager.saveEmail(emailContentManager.getUuid(), emailContentManager.getSender(), emailContentManager.getMessage());
            return true;
        }
        return false;
    }

    public Boolean sendTo(@NotNull File receiverFile, String message, List<String> commands) {
        if (receiverFile.exists()) {
            EmailManager emailManager = new EmailManager(receiverFile);
            EmailContentBean emailContentManager = new EmailContentBean(generateUUID(), playerName, message, false, commands);
            emailManager.saveEmail(emailContentManager.getUuid(), emailContentManager.getSender(), emailContentManager.getMessage(), emailContentManager.getCommands());
            return true;
        }
        return false;
    }

    private void saveEmail(String uuid, String sender, String message) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(uuid + ".sender", sender);
        config.set(uuid + ".message", message);
        config.set(uuid + ".isRead", false);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEmail(String uuid, String sender, String message, List<String> command) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(uuid + ".sender", sender);
        config.set(uuid + ".message", message);
        config.set(uuid + ".isRead", false);
        config.set(uuid + ".command", command);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEmail(String uuid, String sender, String message, @NotNull ItemStack itemStack) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(uuid + ".sender", sender);
        config.set(uuid + ".message", message);
        config.set(uuid + ".isRead", false);
        config.set(uuid + ".isClaimed", false);
        config.set(uuid + ".item", SerializationUtils.serialize(itemStack));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEmail(String uuid, String sender, String message, List<String> command, @NotNull ItemStack itemStack) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(uuid + ".sender", sender);
        config.set(uuid + ".message", message);
        config.set(uuid + ".isRead", false);
        config.set(uuid + ".command", command);
        config.set(uuid + ".isClaimed", false);
        config.set(uuid + ".item", SerializationUtils.serialize(itemStack));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private ItemStack setupItemStack(String uuid) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String itemStackSerialized = config.getString(uuid + ".item");
        return Objects.requireNonNull(SerializationUtils.encodeToItem(itemStackSerialized));
    }

    public Boolean deleteHistoryEmails() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.getKeys(false).size() != 0) {
            if (file.delete()) {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return false;
    }

    public Boolean deleteEmail(String uuid) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(uuid, null);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void sendTeamInvitation() {

    }

    public static @NotNull
    String generateUUID() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }

    public static void sendNotification(@NotNull File receiverFile,@NotNull String message, @Nullable ItemStack itemStack, @Nullable List<String> commands) {
        if (receiverFile.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(receiverFile);
            String uuid = generateUUID();
            config.set(uuid + ".sender", UltiTools.languageUtils.getString("notification"));
            config.set(uuid + ".message", message);
            config.set(uuid + ".isRead", false);
            if (commands != null) {
                config.set(uuid + ".commands", commands);
            }
            config.set(uuid + ".isClaimed", false);
            if (itemStack != null) {
                config.set(uuid + ".item", SerializationUtils.serialize(itemStack));
            }
            try {
                config.save(receiverFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

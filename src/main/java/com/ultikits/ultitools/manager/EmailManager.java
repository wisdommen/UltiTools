package com.ultikits.ultitools.manager;

import com.ultikits.beans.EmailContentBean;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public Boolean sendTo(@NotNull File receiverFile, String message) {
        if (receiverFile.exists()) {
            EmailManager emailManager = new EmailManager(receiverFile);
            EmailContentBean emailContentManager = new EmailContentBean(generateUUID(), playerName, message, false);
            emailManager.saveEmail(emailContentManager.getUuid(), emailContentManager.getSender(), emailContentManager.getMessage());
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
                }catch (IOException e){
                    return false;
                }
            }
        }
        return false;
    }

    public Boolean deleteEmail(String uuid){
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

    public static @NotNull String generateUUID() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }
}

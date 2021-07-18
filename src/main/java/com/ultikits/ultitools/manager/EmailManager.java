package com.ultikits.ultitools.manager;

import com.ultikits.beans.EmailContentBean;
import com.ultikits.ultitools.apis.EmailAPI;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.enums.EmailResponse;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EmailManager implements EmailAPI {

    private final File file;
    private final String playerName;

    public EmailManager(@NotNull OfflinePlayer player) {
        file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player.getName() + ".yml");
        playerName = player.getName();
    }

    public File getFile() {
        return file;
    }

    @Override
    public EmailContentBean getEmail(String uuid){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.getConfigurationSection(uuid) == null){
            return null;
        }
        if (config.getConfigurationSection(uuid).getKeys(false).contains("item")) {
            ItemStack itemStack = setupItemStack(uuid);
            return new EmailContentBean(uuid, config.getString(uuid + ".sender"), config.getString(uuid + ".message"), itemStack, config.getBoolean(uuid + ".isRead"), config.getBoolean(uuid + ".isClaimed"));
        } else {
            return new EmailContentBean(uuid, config.getString(uuid + ".sender"), config.getString(uuid + ".message"), config.getBoolean(uuid + ".isRead"));
        }
    }

    @NotNull
    @Override
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

    @NotNull
    @Override
    public EmailResponse sendTo(@NotNull OfflinePlayer receiver, @Nullable String message, @Nullable ItemStack itemStack, @Nullable List<String> commands) {
        EmailManager emailManager = new EmailManager(receiver);
        if (emailManager.getFile().exists()) {
            EmailContentBean emailContentManager = new EmailContentBean(generateUUID(), playerName, message, commands, itemStack, false, false);
            if (emailManager.saveEmail(emailContentManager.getUuid(), emailContentManager.getSender(), emailContentManager.getMessage(), emailContentManager.getCommands(), emailContentManager.getItemStack())){
                return EmailResponse.SEND_SUCCESS;
            }
            return EmailResponse.SEND_FAILED;
        }
        return EmailResponse.PLAYER_NOTFOUND;
    }

    public EmailResponse sendTo(@NotNull OfflinePlayer receiver, ItemStack itemStack) {
        return sendTo(receiver, null, itemStack, null);
    }

    public EmailResponse sendTo(@NotNull OfflinePlayer receiver, String message) {
        return sendTo(receiver, message, null, null);
    }

    public EmailResponse sendTo(@NotNull OfflinePlayer receiver, String message, List<String> commands) {
        return sendTo(receiver, message, null, commands);
    }

    public EmailResponse sendTo(@NotNull OfflinePlayer receiver, String message, ItemStack itemStack) {
        return sendTo(receiver, message, itemStack, null);
    }

    @Override
    public Boolean saveEmail(String uuid, String sender, @Nullable String message, @Nullable List<String> command, @Nullable ItemStack itemStack) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(uuid + ".sender", sender);
        if (message != null) {
            config.set(uuid + ".message", message);
        }else {
            config.set(uuid + ".message", UltiTools.languageUtils.getString("email_sender_no_message"));
        }
        config.set(uuid + ".isRead", false);
        if (command != null) {
            config.set(uuid + ".command", command);
        }
        if (itemStack != null) {
            config.set(uuid + ".isClaimed", false);
            config.set(uuid + ".item", SerializationUtils.serialize(itemStack));
        }
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @NotNull
    private ItemStack setupItemStack(String uuid) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String itemStackSerialized = config.getString(uuid + ".item");
        return Objects.requireNonNull(SerializationUtils.encodeToItem(itemStackSerialized));
    }

    @Override
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

    @Override
    public void deleteEmail(String uuid) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(uuid, null);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTeamInvitation() {

    }

    public static @NotNull
    String generateUUID() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }

    @Override
    public void sendNotification(@NotNull String message, @Nullable ItemStack itemStack, @Nullable List<String> commands) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
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
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

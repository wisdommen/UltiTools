package com.ultikits.ultitools.apis;

import com.ultikits.beans.EmailContentBean;
import com.ultikits.ultitools.enums.EmailResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface EmailAPI {

    /**
     * 获取一个邮件对象
     *
     * @param uuid 邮件ID
     * @return 一个邮件对象，如果未找到则返回null
     */
    @Nullable EmailContentBean getEmail(String uuid);

    /**
     * 获取此玩家的所有Email
     *
     * @return 此玩家所有的Email，键为Email ID，值为EmailContentBean
     */
    @NotNull Map<String, EmailContentBean> getEmails();

    /**
     * 发送一封邮件
     *
     * @param receiver  收件人
     * @param message   发送的文本信息
     * @param itemStack 发送的附件
     * @param commands  发送的邮件在点击时执行的命令
     * @return EmailResponse 邮件发送后的返回状态
     */
    @NotNull EmailResponse sendTo(@NotNull OfflinePlayer receiver, @Nullable String message, @Nullable ItemStack itemStack, @Nullable List<String> commands);

    /**
     * 删除一封邮件
     *
     * @param uuid 邮件的ID
     */
    void deleteEmail(String uuid);

    /**
     * @return 删除所有历史邮件
     */
    Boolean deleteHistoryEmails();

    /**
     * 将一封邮件持久化保存，发送邮件时自动调用
     *
     * @param uuid      邮件ID
     * @param sender    发送人
     * @param message   文本内容
     * @param command   命令内容
     * @param itemStack 附件内容
     * @return 是否保存成功
     */
    Boolean saveEmail(String uuid, String sender, String message, @Nullable List<String> command, @Nullable ItemStack itemStack);

    /**
     * 给此玩家发送一个通知邮件，不会包含发送人姓名
     *
     * @param message   发送的消息内容
     * @param itemStack 发送的附件内容
     * @param commands  发送的命令内容
     */
    void sendNotification(@NotNull String message, @Nullable ItemStack itemStack, @Nullable List<String> commands);
}

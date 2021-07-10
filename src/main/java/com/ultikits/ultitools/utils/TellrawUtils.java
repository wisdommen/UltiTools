package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.ultitools.UltiTools;

/**
 * Tellraw-可点击消息
 * 发送工具类
 * @author qianmo
 */
public class TellrawUtils {

    /**
     * @param player 目标玩家
     * @param hover 鼠标悬停在文字上时显示的文字
     * @param color 文字颜色
     * @param text 可点击文字
     * @param bold 加粗
     * @param underlined 下划线
     * @param strikethrough 删除线
     * @param obfuscated 随机字符串
     * @param italic 斜体
     * @param action 点击事件
     * @param value 执行目标
     * @param insertion 插入文字
     */
    public void sendClickableText(
            String player,
            String hover,
            String color,
            String text,
            Boolean bold,
            Boolean underlined,
            Boolean strikethrough,
            Boolean obfuscated,
            Boolean italic,
            String action,
            String value,
            String insertion
    ) {
        String json = "tellraw " +
                player +
                "{" +
                "\"text\": \"" +
                text +
                "\"," +
                "\"bold\": " +
                bold +
                "," +
                "\"italic\": " +
                italic +
                "," +
                "\"underlined\": " +
                underlined +
                "," +
                "\"strikethrough\": " +
                strikethrough +
                "," +
                "\"obfuscated\": " +
                obfuscated +
                "," +
                "\"color\": \"" +
                color +
                "\"," +
                "\"insertion\": \"" +
                insertion +
                "\"," +
                "\"clickEvent\": {" +
                "\"action\": \"" +
                action +
                "\"," +
                "\"value\": \"" +
                value +
                "\"" +
                "}," +
                "\"hoverEvent\": {" +
                "\"action\": \"show_text\"," +
                "\"contents\": [\"" +
                hover +
                "\"]" +
                "}" +
                "}";
        UltiTools.getInstance().getServer().dispatchCommand(UltiTools.getInstance().getServer().getConsoleSender(), json);
    }
}

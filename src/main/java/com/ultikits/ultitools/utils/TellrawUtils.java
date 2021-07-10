package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.ultitools.UltiTools;

/**
 * Tellraw-可点击消息
 * 发送工具类
 * <br/>
 * 例子:
 * <pre>
 *     new TellrawUtils()
 *               .player("player")
 *               .hover("hover")
 *               .action("run_command")
 *               .value("/stop")
 *               .text("text")
 *               .send();
 * </pre>
 *
 * @author qianmo
 */
public class TellrawUtils {

    String player;
    String hover = "";
    String color = "white";
    String text;
    Boolean bold = false;
    Boolean underlined = false;
    Boolean strikethrough = false;
    Boolean obfuscated = false;
    Boolean italic = false;
    String action;
    String value;
    String insertion ="";

    public TellrawUtils player(String player) {
        this.player = player;
        return this;
    }

    public TellrawUtils hover(String hover) {
        this.hover = hover;
        return this;
    }

    public TellrawUtils color(String color) {
        this.color = color;
        return this;
    }

    public TellrawUtils text(String text) {
        this.text = text;
        return this;
    }

    public TellrawUtils bold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    public TellrawUtils underlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    public TellrawUtils strikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    public TellrawUtils obfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    public TellrawUtils italic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    public TellrawUtils action(String action) {
        this.action = action;
        return this;
    }

    public TellrawUtils value(String value) {
        this.value = value;
        return this;
    }

    public TellrawUtils insertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    public void send() {
        String json = "tellraw " +
                this.player +
                "{" +
                "\"text\": \"" +
                this.text +
                "\"," +
                "\"bold\": " +
                this.bold +
                "," +
                "\"italic\": " +
                this.italic +
                "," +
                "\"underlined\": " +
                this.underlined +
                "," +
                "\"strikethrough\": " +
                this.strikethrough +
                "," +
                "\"obfuscated\": " +
                this.obfuscated +
                "," +
                "\"color\": \"" +
                this.color +
                "\"," +
                "\"insertion\": \"" +
                this.insertion +
                "\"," +
                "\"clickEvent\": {" +
                "\"action\": \"" +
                this.action +
                "\"," +
                "\"value\": \"" +
                this.value +
                "\"" +
                "}," +
                "\"hoverEvent\": {" +
                "\"action\": \"show_text\"," +
                "\"contents\": [\"" +
                this.hover +
                "\"]" +
                "}" +
                "}";
        UltiTools.getInstance().getServer().dispatchCommand(UltiTools.getInstance().getServer().getConsoleSender(), json);
    }
}

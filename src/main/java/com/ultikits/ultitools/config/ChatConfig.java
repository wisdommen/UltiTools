package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class ChatConfig extends AbstractConfigReviewable{

    private static final ChatConfig config = new ChatConfig("chat", ConfigsEnum.CHAT.toString());

    public ChatConfig() {
        config.init();
    }

    private ChatConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.1;
    }
}

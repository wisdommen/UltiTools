package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class TradeConfig extends AbstractConfigReviewable {
    private static final TradeConfig config = new TradeConfig("trade", ConfigsEnum.TRADE.toString());

    public TradeConfig() {
        config.init();
    }

    private TradeConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.0;
    }
}

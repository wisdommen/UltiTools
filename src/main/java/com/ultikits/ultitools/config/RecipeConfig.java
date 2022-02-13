package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class RecipeConfig extends AbstractConfig {
    private static final RecipeConfig config = new RecipeConfig("recipe", ConfigsEnum.RECIPE.toString());

    public RecipeConfig() {
        config.init();
    }

    private RecipeConfig (String name, String path) {
        super(name, path);
    }
}

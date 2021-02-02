package com.ultikits.ultitools.config;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public abstract class AbstractConfigReviewable extends AbstractConfig {
    Double version;

    AbstractConfigReviewable() {
        super();
    }

    public AbstractConfigReviewable(String name, String filePath) {
        super(name, filePath);
    }

    public AbstractConfigReviewable(String name, String folder, String filePath, String resourcePath) {
        super(name, folder, filePath, resourcePath);
    }

    @Override
    public void init() {
        if (file.exists()) {
            review();
        } else {
            UltiTools.yaml.saveYamlFile(String.valueOf(folder), name + ".yml", resourcePath);
        }
        config = YamlConfiguration.loadConfiguration(file);
        if (!ConfigController.getConfigMap().containsKey(name)){
            ConfigController.registerConfig(name, this);
        }
    }

    public void review() {
        config = YamlConfiguration.loadConfiguration(file);
        if (config.getDouble("config_version") < version) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + resourcePath);
            }
            Reader defConfigStream = new InputStreamReader(in, StandardCharsets.UTF_8);
            YamlConfiguration resourceConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            for (String each : resourceConfig.getKeys(true)) {
                if (!config.contains(each)) {
                    config.set(each, resourceConfig.get(each));
                }
            }
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            reload();
        }
    }

    private InputStream getResource(@NotNull String filename) {
        try {
            URL url = this.getClass().getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }
}

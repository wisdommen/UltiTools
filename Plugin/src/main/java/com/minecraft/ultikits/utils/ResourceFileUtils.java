package com.minecraft.ultikits.utils;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class ResourceFileUtils {

    String fileName = this.getClass().getClassLoader().getResource("文件名").getPath();//获取文件路径
    File file = new File(fileName);
}

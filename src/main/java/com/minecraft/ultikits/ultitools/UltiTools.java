package com.minecraft.ultikits.ultitools;


import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.database.DataBase;
import com.minecraft.economy.database.LinkedDataBase;
import com.minecraft.ultikits.email.Email;
import com.minecraft.ultikits.home.Home;
import com.minecraft.ultikits.joinWelcome.onJoin;
import com.minecraft.ultikits.scoreBoard.runTask;
import com.minecraft.ultikits.scoreBoard.sb_commands;
import com.minecraft.ultikits.whiteList.whitelist_commands;
import com.minecraft.ultikits.whiteList.whitelist_listener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Objects;

public final class UltiTools extends JavaPlugin {

    private static UltiTools plugin;

    public static DataBase dataBase;

    private static UltiEconomy economy;

    public static boolean isPAPILoaded;

    public static UltiEconomy getEconomy() {
        return economy;
    }

    private Boolean setupEconomy(){
        if (getServer().getPluginManager().getPlugin("Economy") == null){
            return false;
        }else {
            economy = new UltiEconomy();
            return true;
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        if (!setupEconomy()){
            getServer().getConsoleSender().sendMessage("无法找到经济前置插件，关闭本插件。。。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        isPAPILoaded = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        File folder = new File(String.valueOf(getDataFolder()));
        File playerDataFolder = new File(getDataFolder() + "/playerData");
        if (!folder.exists()) {
            saveDefaultConfig();
        }
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }

        if (getConfig().getBoolean("enableDataBase")) {
            String username = getConfig().getString("username");
            String password = getConfig().getString("password");
            String host = getConfig().getString("host");
            String database = getConfig().getString("database");
            int port = getConfig().getInt("port");
            String table = "userinfo";

            dataBase = new LinkedDataBase(new String[]{"username", "password", "email", "token", "active", "token_exptime", "regtime", "ban"});

            dataBase.login(host, String.valueOf(port), username, password, database, table);
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "基础插件正在接入数据库...");
            dataBase.connect();
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "基础插件正在初始化数据库...");
            dataBase.createTable();
            dataBase.close();
            getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "基础插件已接入数据库！");
        }

        //注册命令
        Objects.requireNonNull(this.getCommand("email")).setExecutor(new Email());
        Objects.requireNonNull(this.getCommand("home")).setExecutor(new Home());
        Objects.requireNonNull(this.getCommand("sethome")).setExecutor(new Home());
        Objects.requireNonNull(this.getCommand("delhome")).setExecutor(new Home());
        Objects.requireNonNull(this.getCommand("homelist")).setExecutor(new Home());
        Objects.requireNonNull(this.getCommand("wl")).setExecutor(new whitelist_commands());
        Objects.requireNonNull(this.getCommand("sb")).setExecutor(new sb_commands());

        //注册监听器
        Bukkit.getPluginManager().registerEvents(new onJoin(), this);
        if (getConfig().getBoolean("enable_white_list")) {
            Bukkit.getPluginManager().registerEvents(new whitelist_listener(), this);
        }

        //注册任务
        BukkitTask t1 = new runTask().runTaskTimer(this, 0, 20L);

        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "基础插件已加载！");
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "作者：wisdomme");

    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "基础插件已卸载！");
    }

    public static UltiTools getInstance() {
        return plugin;
    }
}

package com.minecraft.ultikits.ultitools;


import com.minecraft.Ultilevel.level.listener.chat;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.database.DataBase;
import com.minecraft.economy.database.LinkedDataBase;
import com.minecraft.ultikits.GUIs.GUISetup;
import com.minecraft.ultikits.UpdateChecker.ConfigFileChecker;
import com.minecraft.ultikits.UpdateChecker.VersionChecker;
import com.minecraft.ultikits.chestLock.ChestLock;
import com.minecraft.ultikits.chestLock.ChestLockCMD;
import com.minecraft.ultikits.email.Email;
import com.minecraft.ultikits.home.Home;
import com.minecraft.ultikits.joinWelcome.onJoin;
import com.minecraft.ultikits.multiworlds.multiWorlds;
import com.minecraft.ultikits.prefix.Chat;
import com.minecraft.ultikits.remoteChest.ChestPage;
import com.minecraft.ultikits.remoteChest.RemoteBagCMD;
import com.minecraft.ultikits.scoreBoard.runTask;
import com.minecraft.ultikits.scoreBoard.sb_commands;
import com.minecraft.ultikits.whiteList.whitelist_commands;
import com.minecraft.ultikits.whiteList.whitelist_listener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Objects;

public final class UltiTools extends JavaPlugin {

    private static UltiTools plugin;
    public static DataBase dataBase;
    private static UltiEconomy economy;
    public static boolean isPAPILoaded;
    private static Economy econ = null;
    private static Boolean isVaultInstalled;

    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static Boolean getIsVaultInstalled() {
        return isVaultInstalled;
    }

    public static UltiEconomy getEconomy() {
        return economy;
    }

    private Boolean setupEconomy(){
        if (getServer().getPluginManager().getPlugin("Economy") != null){
            economy = new UltiEconomy();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        Boolean economyEnabled = setupEconomy();
        isVaultInstalled = setupVault();

        if (!economyEnabled && !isVaultInstalled){
            getLogger().info(ChatColor.RED + "UltiTools插件未找到经济插件，关闭中...");
            getLogger().info(ChatColor.RED + "UltiTools插件至少需要Vault或者UltiEconomy才能运行");
            getServer().getPluginManager().disablePlugin(this);
        }

        isPAPILoaded = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        File folder = new File(String.valueOf(getDataFolder()));
        File playerDataFolder = new File(getDataFolder() + "/playerData");
        File config_file = new File(getDataFolder(), "config.yml");
        if (!folder.exists() || !config_file.exists()) {
            saveDefaultConfig();
        }
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
        ConfigFileChecker.reviewConfigFile();

        if (getConfig().getBoolean("enableDataBase")) {
            String username = getConfig().getString("username");
            String password = getConfig().getString("password");
            String host = getConfig().getString("host");
            String database = getConfig().getString("database");
            int port = getConfig().getInt("port");
            String table = "userinfo";

            dataBase = new LinkedDataBase(new String[]{"username", "password", "email", "token", "active", "token_exptime", "regtime", "ban"});

            dataBase.login(host, String.valueOf(port), username, password, database, table);
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件正在接入数据库...");
            dataBase.connect();
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件正在初始化数据库...");
            dataBase.createTable();
            dataBase.close();
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件已接入数据库！");
        }

        //注册命令
        if (this.getConfig().getBoolean("enable_email")) {
            Objects.requireNonNull(this.getCommand("email")).setExecutor(new Email());
        }
        if (this.getConfig().getBoolean("enable_home")) {
            Objects.requireNonNull(this.getCommand("home")).setExecutor(new Home());
            Objects.requireNonNull(this.getCommand("sethome")).setExecutor(new Home());
            Objects.requireNonNull(this.getCommand("delhome")).setExecutor(new Home());
            Objects.requireNonNull(this.getCommand("homelist")).setExecutor(new Home());
        }
        if (this.getConfig().getBoolean("enable_white_list")) {
            if (economyEnabled) {
                Objects.requireNonNull(this.getCommand("wl")).setExecutor(new whitelist_commands());
            }else {
                getLogger().info(ChatColor.RED + "未找到UltiEconomy插件，关闭白名单功能");
                getLogger().info(ChatColor.RED + "白名单功能需要配合UltiEconomy使用");
            }
        }
        if (this.getConfig().getBoolean("enable_scoreboard")) {
            Objects.requireNonNull(this.getCommand("sb")).setExecutor(new sb_commands());
        }
        if (this.getConfig().getBoolean("enable_lock")) {
            Objects.requireNonNull(this.getCommand("lock")).setExecutor(new ChestLockCMD());
            Objects.requireNonNull(this.getCommand("unlock")).setExecutor(new ChestLockCMD());
        }
        if (this.getConfig().getBoolean("enable_remote_chest")) {
            Objects.requireNonNull(this.getCommand("bag")).setExecutor(new RemoteBagCMD());
        }
        if (this.getConfig().getBoolean("enable_multiworlds")) {
            Objects.requireNonNull(this.getCommand("mw")).setExecutor(new multiWorlds());
        }

        //注册监听器
        if (getConfig().getBoolean("enable_onjoin")) {
            Bukkit.getPluginManager().registerEvents(new onJoin(), this);
        }
        if (getConfig().getBoolean("enable_white_list")) {
            Bukkit.getPluginManager().registerEvents(new whitelist_listener(), this);
        }
        if (this.getConfig().getBoolean("enable_lock")) {
            Bukkit.getPluginManager().registerEvents(new ChestLock(), this);
        }
        if (this.getConfig().getBoolean("enable_remote_chest")) {
            Bukkit.getPluginManager().registerEvents(new ChestPage(), this);
        }
        if (getConfig().getBoolean("enable_chat")) {
            getServer().getPluginManager().registerEvents(new Chat(), this);
        }

        //注册任务
        if (this.getConfig().getBoolean("enable_scoreboard")) {
            BukkitTask t1 = new runTask().runTaskTimer(this, 0, 20L);
        }

        //初始化GUI
        if (this.getConfig().getBoolean("enable_remote_chest")) {
            GUISetup.setUpGUIs();
        }

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件已加载！");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "作者：wisdomme");

        //检查更新
        if (getConfig().getBoolean("enable_version_check")) {
            VersionChecker.setupThread();
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件已卸载！");
    }

    public static UltiTools getInstance() {
        return plugin;
    }
}

package com.minecraft.ultikits.ultitools;


import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.ultikits.UpdateChecker.ConfigFileChecker;
import com.minecraft.ultikits.UpdateChecker.VersionChecker;
import com.minecraft.ultikits.chestLock.ChestLock;
import com.minecraft.ultikits.chestLock.ChestLockCMD;
import com.minecraft.ultikits.commands.ToolsCommands;
import com.minecraft.ultikits.email.Email;
import com.minecraft.ultikits.email.EmailPage;
import com.minecraft.ultikits.home.Home;
import com.minecraft.ultikits.joinWelcome.onJoin;
import com.minecraft.ultikits.kits.KitsCommands;
import com.minecraft.ultikits.kits.KitsPage;
import com.minecraft.ultikits.login.LoginGUI;
import com.minecraft.ultikits.login.LoginListener;
import com.minecraft.ultikits.multiworlds.multiWorlds;
import com.minecraft.ultikits.prefix.Chat;
import com.minecraft.ultikits.remoteChest.ChestPage;
import com.minecraft.ultikits.remoteChest.RemoteBagCMD;
import com.minecraft.ultikits.scoreBoard.NamePrefixSuffix;
import com.minecraft.ultikits.scoreBoard.SideBar;
import com.minecraft.ultikits.scoreBoard.sb_commands;
import com.minecraft.ultikits.utils.DatabaseUtils;
import com.minecraft.ultikits.whiteList.whitelist_commands;
import com.minecraft.ultikits.whiteList.whitelist_listener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.minecraft.ultikits.kits.KitsCommands.initFile;
import static com.minecraft.ultikits.login.LoginListener.playerLoginStatus;
import static com.minecraft.ultikits.login.LoginListener.savePlayerLoginStatus;
import static com.minecraft.ultikits.utils.DatabasePlayerTools.getIsLogin;

public final class UltiTools extends JavaPlugin {

    private static UltiTools plugin;
    private static UltiEconomy economy;
    public static boolean isPAPILoaded;
    public static boolean isUltiEconomyInstalled;
    private static Economy econ = null;
    private static Boolean isVaultInstalled;
    public static boolean isDatabaseEnabled;

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

    private Boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("UltiEconomy") != null) {
            economy = new UltiEconomy();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        isUltiEconomyInstalled = setupEconomy();
        isVaultInstalled = setupVault();

        isDatabaseEnabled = getConfig().getBoolean("enableDataBase");

        isPAPILoaded = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (!isPAPILoaded) {
            getLogger().warning("UltiTools插件未找到PAPI前置插件，查找其他可行依赖中...");
            if (!(isUltiEconomyInstalled || isVaultInstalled)) {
                getLogger().warning("UltiTools插件未找到经济前置插件，关闭中...");
                getLogger().warning("UltiTools插件至少需要Vault或者UltiEconomy, 或者安装PAPI才能运行");
                getServer().getPluginManager().disablePlugin(this);
            }
            if (getServer().getPluginManager().getPlugin("UltiLevel") == null) {
                getLogger().warning("UltiTools插件未找到UltiLevel等级插件，关闭计分板等级相关显示！");
            }
        }

        File folder = new File(String.valueOf(getDataFolder()));
        List<File> folders = new ArrayList<>();
        folders.add(new File(getDataFolder() + "/playerData"));
        folders.add(new File(getDataFolder() + "/chestData"));
        folders.add(new File(getDataFolder() + "/loginData"));
        folders.add(new File(getDataFolder() + "/emailData"));
        File config_file = new File(getDataFolder(), "config.yml");
        if (!folder.exists() || !config_file.exists()) {
            saveDefaultConfig();
        }
        initFile();
        makedirs(folders);
        ConfigFileChecker.reviewConfigFile();

        if (isDatabaseEnabled) {
            String table = "userinfo";
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件正在初始化数据库...");
            if(DatabaseUtils.createTable(table, new String[]{"username", "password", "whitelisted", "banned"})){
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件接入数据库成功！");
            }else {
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件接入数据库失败！");
                getConfig().set("enableDataBase", false);
                saveConfig();
                reloadConfig();
            }
        }

        //加载世界
        if (this.getConfig().getBoolean("enable_multiworlds")) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "正在加载世界中...");
            File worldFile = new File(getDataFolder(), "worlds.yml");
            YamlConfiguration worldConfig = YamlConfiguration.loadConfiguration(worldFile);
            List<String> worlds = worldConfig.getStringList("worlds");
            for (String eachWorld : worlds){
                getServer().createWorld(new WorldCreator(eachWorld));
            }
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"世界加载成功！");
        }

        //注册命令
        Objects.requireNonNull(this.getCommand("ultitools")).setExecutor(new ToolsCommands());
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
                Objects.requireNonNull(this.getCommand("wl")).setExecutor(new whitelist_commands());
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
        if (this.getConfig().getBoolean("enable_kits")) {
            Objects.requireNonNull(this.getCommand("kits")).setExecutor(new KitsCommands());
        }

        //注册监听器
        if (this.getConfig().getBoolean("enable_email")) {
            Bukkit.getPluginManager().registerEvents(new EmailPage(), this);
        }
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
        if (getConfig().getBoolean("enable_login")) {
            getServer().getPluginManager().registerEvents(new LoginListener(), this);
            getServer().getPluginManager().registerEvents(new LoginGUI(), this);
        }
        if (this.getConfig().getBoolean("enable_kits")) {
            getServer().getPluginManager().registerEvents(new KitsPage(), this);
        }

        //注册任务
        if (this.getConfig().getBoolean("enable_scoreboard")) {
            BukkitTask t1 = new SideBar().runTaskTimer(this, 0, 20L);
        }
        if (this.getConfig().getBoolean("enable_name_prefix")) {
            BukkitTask t2 = new NamePrefixSuffix().runTaskTimer(this, 0, 20L);
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
        for (String player : playerLoginStatus.keySet()){
            if (Bukkit.getPlayerExact(player)!=null){
                Player player1 = Bukkit.getPlayerExact(player);
                assert player1 != null;
                if (!getIsLogin(player1)){
                    player1.kickPlayer(ChatColor.AQUA+"腐竹重载/关闭了插件，请重新登录！");
                }
            }
        }
        savePlayerLoginStatus();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "基础插件已卸载！");
    }

    public static UltiTools getInstance() {
        return plugin;
    }

    private void makedirs(List<File> folders){
        for (File eachFolder : folders){
            if (!eachFolder.exists()) {
                eachFolder.mkdirs();
            }
        }
    }
}

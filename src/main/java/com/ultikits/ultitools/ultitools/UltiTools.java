package com.ultikits.ultitools.ultitools;

import com.ultikits.api.VersionWrapper;
import com.ultikits.checker.ProChecker;
import com.ultikits.main.UltiCoreAPI;
import com.ultikits.ultitools.checker.PlayerlistChecker;
import com.ultikits.ultitools.checker.VersionChecker;
import com.ultikits.ultitools.commands.*;
import com.ultikits.ultitools.listener.*;
import com.ultikits.ultitools.register.CommandRegister;
import com.ultikits.ultitools.services.ChestLockService;
import com.ultikits.ultitools.services.ScoreBoardService;
import com.ultikits.ultitools.utils.*;
import com.ultikits.utils.Metrics;
import com.ultikits.utils.VersionAdaptor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;

import java.io.File;
import java.util.*;

import static com.ultikits.ultitools.listener.LoginListener.savePlayerLoginStatus;
import static com.ultikits.ultitools.services.DatabasePlayerService.getIsLogin;

public final class UltiTools extends JavaPlugin {

    public  static UltiCoreAPI    ultiCoreAPI;
    public  static boolean        isGroupManagerEnabled;
    public  static boolean        isPAPILoaded;
    public  static LanguageUtils  languageUtils;
    public  static VersionWrapper versionAdaptor;
    public  static YamlFileUtils  yaml;
    public  static String         language;
    public  static boolean        isDatabaseEnabled;
    private static ProChecker     proChecker;
    private static boolean        isUltiCoreUpToDate;
    private static UltiTools      plugin;
    private static Initializer    initializer;
    private static SessionFactory sessionFactory;

    @Override
    public void onLoad() {
        initializer = new Initializer(this);
        //输出 Banner
        initializer.sendBanner();
        //依赖检查
        isUltiCoreUpToDate = initializer.checkCore();
    }

    @Override
    public void onEnable() {
        //初始化变量
        plugin             = this;
        versionAdaptor     = new VersionAdaptor().match();
        ultiCoreAPI        = new UltiCoreAPI(this);
        isPAPILoaded       = UltiCoreAPI.isPapiLoaded();
        Metrics metrics    = new Metrics(this, 8652);
        File folder        = new File(String.valueOf(getDataFolder()));
        yaml               = new YamlFileUtils();
        List<File> folders = new ArrayList<>();
        File config_file   = new File(getDataFolder(), "config.yml");

        //初始化 Metrics
        initializer.initMetrics(metrics);
        //语言初始化
        initializer.initLanguage(folder, config_file);
        //配置文件初始化
        initializer.initConfig(folders);
        //数据库初始化
        sessionFactory = initializer.initDataBase();
        //初始化 Pro
        proChecker = initializer.initPro();
        //初始化 PAPI
        initializer.initPAPI();
        //加载世界
        initializer.initWorld();

        //监听器&执行器扫描
        ScanUtils.scanListeners("com.ultikits.ultitools.listener", this);
        ScanUtils.scanCommands("com.ultikits.ultitools.commands", this);

        //核心命令注册
        Objects.requireNonNull(this.getCommand("ultitools")).setExecutor(new ToolsCommands());
        if (getConfig().getBoolean("enable_command-alias_function")) {
            for (String alia : new CommandListener().getCommandAliasList()) {
                CommandRegister.registerCommand(this, null, null, "", alia);
            }
        }

        //注册任务
        initializer.runTasks();

        //加载完毕
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("plugin_loaded"));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("author") + "wisdomme");

        //检查更新
        if (getConfig().getBoolean("enable_version_check")) VersionChecker.runTask();

        //旧数据转换
        ChestLockService.transformOldData();
        new PlayerlistChecker().playerlistNewChecker();
    }

    @Override
    public void onDisable() {
        if (!isUltiCoreUpToDate) return;
        if (getConfig().getBoolean("enable_login")) {
            for (String player : LoginListener.playerLoginStatus.keySet()) {
                if (Bukkit.getPlayerExact(player) == null) continue;
                Player player1 = Bukkit.getPlayerExact(player);
                assert player1 != null;
                if (!getIsLogin(player1)) player1.kickPlayer(ChatColor.AQUA + "[UltiTools Login] " + languageUtils.getString("login_plugin_reloaded"));
            }
            savePlayerLoginStatus();
        }
        if (this.getConfig().getBoolean("enable_scoreboard")) ScoreBoardService.clearScoreboards();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("plugin_disabled"));
    }

    public static UltiTools getInstance() {
        return plugin;
    }

    public ProChecker getProChecker() {
        return proChecker;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

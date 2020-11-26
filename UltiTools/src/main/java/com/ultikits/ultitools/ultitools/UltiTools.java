package com.ultikits.ultitools.ultitools;

import com.ultikits.api.VersionWrapper;
import com.ultikits.beans.CheckResponse;
import com.ultikits.main.UltiCoreAPI;
import com.ultikits.ultitools.checker.prochecker.ProChecker;
import com.ultikits.ultitools.checker.updatechecker.ConfigFileChecker;
import com.ultikits.ultitools.checker.updatechecker.VersionChecker;
import com.ultikits.ultitools.commands.*;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.listener.*;
import com.ultikits.ultitools.register.CommandRegister;
import com.ultikits.ultitools.tasks.*;
import com.ultikits.ultitools.utils.YamlFileUtils;
import com.ultikits.utils.DatabaseUtils;
import com.ultikits.utils.LanguageUtils;
import com.ultikits.utils.VersionAdaptor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.ultikits.ultitools.listener.LoginListener.savePlayerLoginStatus;
import static com.ultikits.ultitools.utils.DatabasePlayerTools.getIsLogin;

public final class UltiTools extends JavaPlugin {

    public static UltiCoreAPI ultiCoreAPI;
    public static boolean isGroupManagerEnabled;
    public static boolean isPAPILoaded;
    private static UltiTools plugin;
    public static LanguageUtils languageUtils;
    public static VersionWrapper versionAdaptor = new VersionAdaptor().match();
    public static YamlFileUtils yaml;
    public static String language;
    public static boolean isProVersion;
    public static boolean isDatabaseEnabled;

    @Override
    public void onEnable() {
        plugin = this;
        ultiCoreAPI = new UltiCoreAPI(this, this.getDataFolder().getAbsolutePath());
        isPAPILoaded = UltiCoreAPI.isPapiLoaded();
        ultiCoreAPI.startBStates(8652);

        File folder = new File(String.valueOf(getDataFolder()));
        File config_file = new File(getDataFolder(), "config.yml");
        yaml = new YamlFileUtils();
        if (!folder.exists() || !config_file.exists()) {
            folder.mkdirs();
            setLocalLanguage();
            yaml.saveYamlFile(getDataFolder().getPath(), "config.yml", language + "_config.yml");
        }

        setLocalLanguage();
        List<File> folders = new ArrayList<>();
        folders.add(new File(getDataFolder() + "/playerData"));
        folders.add(new File(getDataFolder() + "/chestData"));
        folders.add(new File(getDataFolder() + "/loginData"));
        folders.add(new File(getDataFolder() + "/emailData"));
        folders.add(new File(getDataFolder() + "/permission"));
        folders.add(new File(getDataFolder() + "/sidebar"));
        folders.add(new File(getDataFolder() + "/kitData"));

        makedirs(folders);

        languageUtils = new LanguageUtils(getDataFolder()+ File.separator+"lang", getConfig().getString("language").toLowerCase());

        ConfigController.initFiles();
        ConfigFileChecker.reviewMainConfigFile();

        String la;
        if (getConfig().getString("language") == null) {
            la = languageUtils.getLanguage();
        } else {
            la = getConfig().getString("language");
            languageUtils.setLanguage(la);
        }
        language = la.split("_")[0];

        isDatabaseEnabled = getConfig().getBoolean("enableDataBase");

        if (isDatabaseEnabled) {
            String table = "userinfo";
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getWords("initializing_database"));
            String ip = getConfig().getString("host");
            String port = getConfig().getString("port");
            String username = getConfig().getString("username");
            String password = getConfig().getString("password");
            String database = getConfig().getString("database");
            ultiCoreAPI.setUpDatabase(database, ip, port, username, password);
            if (DatabaseUtils.createTable(table, new String[]{"username", "password", "whitelisted", "banned"})) {
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getWords("database_connected"));
            } else {
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getWords("database_connect_failed"));
                isDatabaseEnabled = false;
            }
        }

        if (getConfig().getBoolean("enable_pro")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (UltiTools.getInstance().getConfig().getBoolean("enable_pro")) {
                        try {
                            CheckResponse res = ProChecker.run();
                            if (res.code.equals("200")) {
                                UltiTools.isProVersion = true;
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + languageUtils.getWords("pro_validated"));
                            } else {
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] " + languageUtils.getWords("pro_validation_failed"));
                            }
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] " + res.msg);
                        } catch (Exception e) {
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] " + languageUtils.getWords("pro_validation_failed"));
                        }
                    }
                }
            }.runTaskAsynchronously(plugin);
        }

        if (!isPAPILoaded) {
            getLogger().warning("[UltiTools] " + languageUtils.getWords("papi_not_found"));
            if (getServer().getPluginManager().getPlugin("UltiLevel") == null) {
                getLogger().warning("[UltiTools] " + languageUtils.getWords("ultilevel_not_found"));
            }
        }

        //加载世界
        if (this.getConfig().getBoolean("enable_multiworlds")) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getWords("loading_worlds"));
            File worldFile = new File(getDataFolder(), "worlds.yml");
            YamlConfiguration worldConfig = YamlConfiguration.loadConfiguration(worldFile);
            List<String> worlds = worldConfig.getStringList("worlds");
            for (String eachWorld : worlds) {
                getServer().createWorld(new WorldCreator(eachWorld));
            }
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getWords("worlds_load_successfully"));
        }

        //Objects.requireNonNull(this.getCommand("ultitools")).setExecutor(new ToolsCommands());
        if (this.getConfig().getBoolean("enable_email")) {
            CommandRegister.registerCommand(plugin, new EmailCommands(), "ultikits.tools.email", languageUtils.getWords("email_function"), "email");
        }
        if (this.getConfig().getBoolean("enable_home")) {
            CommandRegister.registerCommand(plugin, new HomeCommands(), "ultikits.tools.home", languageUtils.getWords("home_function"), "home");
            CommandRegister.registerCommand(plugin, new SetHomeCommands(), "ultikits.tools.sethome", languageUtils.getWords("sethome_function"), "sethome");
            CommandRegister.registerCommand(plugin, new DeleteHomeCommands(), "ultikits.tools.delhome", languageUtils.getWords("delhome_function"), "delhome");
            CommandRegister.registerCommand(plugin, new HomeListCommands(), "ultikits.tools.homelist", languageUtils.getWords("listhome_function"), "homelist");
            getServer().getPluginManager().registerEvents(new HomeCommands(), this);
        }
        if (this.getConfig().getBoolean("enable_white_list")) {
            CommandRegister.registerCommand(plugin, new WhitelistCommands(), "ultikits.tools.whitelist", languageUtils.getWords("whitelist_function"), "wl");
            Bukkit.getPluginManager().registerEvents(new WhitelistListener(), this);
        }
        if (this.getConfig().getBoolean("enable_scoreboard")) {
            CommandRegister.registerCommand(plugin, new SbCommands(), "ultikits.tools.scoreboard", languageUtils.getWords("sidebar_function"), "sb");
            new SideBarTask().runTaskTimer(this, 0, 20L);
        }
        if (this.getConfig().getBoolean("enable_lock")) {
            CommandRegister.registerCommand(plugin, new UnlockCommands(), "ultikits.tools.lock", languageUtils.getWords("lock_chest_function"), "unlock");
            CommandRegister.registerCommand(plugin, new LockCommands(), "ultikits.tools.unlock", languageUtils.getWords("unlock_chest_function"), "lock");
            Bukkit.getPluginManager().registerEvents(new ChestLockListener(), this);
        }
        if (this.getConfig().getBoolean("enable_remote_chest")) {
            CommandRegister.registerCommand(plugin, new RemoteBagCommands(), "ultikits.tools.bag", languageUtils.getWords("bag_function"), "bag");
            CommandRegister.registerCommand(plugin, new RemoteBagConsoleCommands(), "ultikits.tools.admin", languageUtils.getWords("bag_console_function"), "createbag");
        }
        if (this.getConfig().getBoolean("enable_multiworlds")) {
            CommandRegister.registerCommand(plugin, new MultiWorldsCommands(), "ultikits.tools.mw", languageUtils.getWords("multiworlds_function"), "mw");
        }
        if (this.getConfig().getBoolean("enable_kits")) {
            CommandRegister.registerCommand(plugin, new KitsCommands(), "ultikits.tools.kits", languageUtils.getWords("kits_function"), "kits");
        }
        if (this.getConfig().getBoolean("enable_cleaner")) {
            CommandRegister.registerCommand(plugin, new CleanerCommands(), "ultikits.tools.clean", languageUtils.getWords("cleaner_function"), "clean");
        }
        if (this.getConfig().getBoolean("enable_permission")) {
            CommandRegister.registerCommand(plugin, new PermissionCommands(), "ultikits.tools.permission", languageUtils.getWords("permission_function"), "pers");
            getServer().getPluginManager().registerEvents(new PermissionAddOnJoinListener(), this);
        }


        //注册监听器
        if (getConfig().getBoolean("enable_onjoin")) {
            Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        }
        if (getConfig().getBoolean("enable_chat")) {
            getServer().getPluginManager().registerEvents(new ChatListener(), this);
        }
        if (getConfig().getBoolean("enable_login")) {
            getServer().getPluginManager().registerEvents(new LoginListener(), this);
            getServer().getPluginManager().registerEvents(new LoginGUIListener(), this);
            getServer().getPluginManager().registerEvents(new ValidationPageListener(), this);
            LoginListener.checkPlayerAlreadyLogin();
            CommandRegister.registerCommand(plugin, new LoginRegisterCommands(), "ultikits.tools.login", languageUtils.getWords("login_function"), "reg", "regs", "re");
        }
        if (getConfig().getBoolean("enable_death_punishment")) {
            getServer().getPluginManager().registerEvents(new DeathListener(), this);
        }

        //注册任务
        if (this.getConfig().getBoolean("enable_name_prefix")) {
            new NamePrefixSuffixTask().runTaskTimer(this, 0, 20L);
        }
        if (this.getConfig().getBoolean("enable_cleaner")) {
            new CleanerTask().runTaskTimerAsynchronously(this, 10 * 20L, 10 * 20L);
            new UnloadChunksTask().runTaskTimer(this, 0L, 60 * 20L);
        }
        if (getConfig().getBoolean("enable_pro")) {
            new ProCheckerTask().runTaskTimerAsynchronously(this, 12000L, 12000L);
        }

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getWords("plugin_loaded"));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getWords("author") + "wisdomme");

        //检查更新
        if (getConfig().getBoolean("enable_version_check")) {
            VersionChecker.runTask();
        }
    }

    @Override
    public void onDisable() {
        for (String player : LoginListener.playerLoginStatus.keySet()) {
            if (Bukkit.getPlayerExact(player) != null) {
                Player player1 = Bukkit.getPlayerExact(player);
                assert player1 != null;
                if (!getIsLogin(player1)) {
                    player1.kickPlayer(ChatColor.AQUA + "[UltiTools Login] " + languageUtils.getWords("login_plugin_reloaded"));
                }
            }
        }
        savePlayerLoginStatus();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getWords("plugin_disabled"));
    }

    public static UltiTools getInstance() {
        return plugin;
    }

    private void setLocalLanguage() {
        Locale defaultLocale = Locale.getDefault();
        List<String> langs = Arrays.asList("en", "zh");
        language = defaultLocale.getLanguage();
        if (!langs.contains(language)) {
            language = "en";
        }
    }

    private void makedirs(List<File> folders) {
        for (File eachFolder : folders) {
            if (!eachFolder.exists()) {
                eachFolder.mkdirs();
            }
        }
    }
}

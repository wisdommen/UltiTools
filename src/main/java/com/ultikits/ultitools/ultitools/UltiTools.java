package com.ultikits.ultitools.ultitools;

import com.ultikits.api.VersionWrapper;
import com.ultikits.main.UltiCoreAPI;
import com.ultikits.ultitools.checker.DependencyChecker;
import com.ultikits.ultitools.checker.NewProChecker;
import com.ultikits.ultitools.checker.VersionChecker;
import com.ultikits.ultitools.commands.*;
import com.ultikits.ultitools.config.*;
import com.ultikits.ultitools.listener.*;
import com.ultikits.ultitools.register.CommandRegister;
import com.ultikits.ultitools.tasks.*;
import com.ultikits.ultitools.utils.YamlFileUtils;
import com.ultikits.utils.DatabaseUtils;
import com.ultikits.utils.MessagesUtils;
import com.ultikits.utils.VersionAdaptor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

import static com.ultikits.ultitools.listener.LoginListener.savePlayerLoginStatus;
import static com.ultikits.ultitools.tasks.SideBarTask.clearScoreboards;
import static com.ultikits.ultitools.utils.DatabasePlayerTools.getIsLogin;

public final class UltiTools extends JavaPlugin {

    public static UltiCoreAPI ultiCoreAPI;
    public static boolean isGroupManagerEnabled;
    public static boolean isPAPILoaded;
    private static UltiTools plugin;
    public static YamlConfiguration languageUtils;
    public static VersionWrapper versionAdaptor = new VersionAdaptor().match();
    public static YamlFileUtils yaml;
    public static String language;
    public static boolean isProVersion;
    public static boolean isDatabaseEnabled;
    public static DatabaseUtils databaseUtils;
    private static boolean isUltiCoreUpToDate;

    @Override
    public void onEnable() {
        plugin = this;
        isUltiCoreUpToDate = DependencyChecker.isUltiCoreUpToDate();
        if (!isUltiCoreUpToDate){
            this.getServer().getConsoleSender().sendMessage(MessagesUtils.warning("The version of UltiCoreAPI is too old to enable UltiTools!"));
            this.getServer().getConsoleSender().sendMessage(MessagesUtils.warning("Use [/ulticore upgrade] to download the newest version of UltiCoreAPI!"));
            this.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        ultiCoreAPI = new UltiCoreAPI(this);
        isPAPILoaded = UltiCoreAPI.isPapiLoaded();
        ultiCoreAPI.startBStates(8652);

        File folder = new File(String.valueOf(getDataFolder()));
        File config_file = new File(getDataFolder(), "config.yml");
        yaml = new YamlFileUtils();
        setLocalLanguage();
        if (!folder.exists() || !config_file.exists()) {
            folder.mkdirs();
            yaml.saveYamlFile(getDataFolder().getPath(), "config.yml", language + "_config.yml");
        }
        language = getConfig().getString("language").split("_")[0];
        String cusLang = getConfig().getString("language").split("_")[1];
        yaml.saveYamlFile(getDataFolder().getPath() + File.separator + "lang", language + ".yml", language + ".yml", true);

        if (cusLang.equalsIgnoreCase("cn")||cusLang.equalsIgnoreCase("us")) {
            File langFile = new File(getDataFolder().getPath() + File.separator + "lang", language + ".yml");
            languageUtils = YamlConfiguration.loadConfiguration(langFile);
        }else {
            File langFile = new File(getDataFolder().getPath() + File.separator + "lang", cusLang + ".yml");
            languageUtils = YamlConfiguration.loadConfiguration(langFile);
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("using_customized_language"));
        }

        List<File> folders = new ArrayList<>();
        folders.add(new File(getDataFolder().getPath()));
        folders.add(new File(getDataFolder() + "/playerData"));
        folders.add(new File(getDataFolder() + "/chestData"));
        folders.add(new File(getDataFolder() + "/loginData"));
        folders.add(new File(getDataFolder() + "/emailData"));
        folders.add(new File(getDataFolder() + "/permission"));
        folders.add(new File(getDataFolder() + "/sidebar"));
        folders.add(new File(getDataFolder() + "/kitData"));
        folders.add(new File(getDataFolder() + "/warps"));

        makedirs(folders);

        Arrays.asList(
                new KitsConfig(),
                new CleanerConfig(),
//                new GroupPermissionConfig(),
//                new UserPermissionConfig(),
//                new GlobuleGroupsConfig(),
                new LoginConfig(),
                new JoinWelcomeConfig(),
                new SideBarConfig(),
                new SideBarDataConfig(),
                new ChestLockConfig(),
                new HomeConfig(),
                new ChestDataConfig(),
                new MultiworldsConfig(),
                new DeathPunishConfig(),
                new MainConfig(),
                new WhiteListConfig(),
                new BagConfig(),
                new ChatConfig(),
                new CustomerGUIConfig()
        );

        isDatabaseEnabled = getConfig().getBoolean("enableDataBase");

        if (isDatabaseEnabled) {
            String table = "userinfo";
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("initializing_database"));
            String ip = getConfig().getString("host");
            String port = getConfig().getString("port");
            String username = getConfig().getString("username");
            String password = getConfig().getString("password");
            String database = getConfig().getString("database");
            ultiCoreAPI.setUpDatabase(database, ip, port, username, password);
            databaseUtils = new DatabaseUtils(ultiCoreAPI);

            if (databaseUtils.createTable(table, new String[]{"username", "password", "whitelisted", "banned"})
                    && databaseUtils.createTable(table, new String[]{"username", "friends", "black_list"})) {
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("database_connected"));
            } else {
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("database_connect_failed"));
                isDatabaseEnabled = false;
            }
        }

        if (getConfig().getBoolean("enable_pro")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (UltiTools.getInstance().getConfig().getBoolean("enable_pro")) {
                        try {
                            String res = NewProChecker.validatePro();
                            if (res.equals("Pro Version Activated!")) {
                                UltiTools.isProVersion = true;
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + languageUtils.getString("pro_validated"));
                            } else {
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] " + languageUtils.getString("pro_validation_failed"));
                            }
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + res);
                        } catch (Exception e) {
                            e.printStackTrace();
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] " + languageUtils.getString("pro_validation_failed"));
                        }
                    }
                }
            }.runTaskAsynchronously(plugin);
        }

        if (!isPAPILoaded) {
            getLogger().warning("[UltiTools] " + languageUtils.getString("papi_not_found"));
            if (getServer().getPluginManager().getPlugin("UltiLevel") == null) {
                getLogger().warning("[UltiTools] " + languageUtils.getString("ultilevel_not_found"));
            }
        }

        //加载世界
        if (this.getConfig().getBoolean("enable_multiworlds")) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("loading_worlds"));
            File worldFile = new File(getDataFolder(), "worlds.yml");
            YamlConfiguration worldConfig = YamlConfiguration.loadConfiguration(worldFile);
            List<String> worlds = worldConfig.getStringList("worlds");
            for (String eachWorld : worlds) {
                getServer().createWorld(new WorldCreator(eachWorld));
            }
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("worlds_load_successfully"));
        }

        Objects.requireNonNull(this.getCommand("ultitools")).setExecutor(new ToolsCommands());
        if (this.getConfig().getBoolean("enable_email")) {
            CommandRegister.registerCommand(plugin, new EmailCommands(), "ultikits.tools.email", languageUtils.getString("email_function"), "email", "ultimail", "mail", "mails");
            getServer().getPluginManager().registerEvents(new EmailPageListener(), this);
        }
        if (this.getConfig().getBoolean("enable_home")) {
            CommandRegister.registerCommand(plugin, new HomeCommands(), "ultikits.tools.home", languageUtils.getString("home_function"), "home", "h");
            CommandRegister.registerCommand(plugin, new SetHomeCommands(), "ultikits.tools.sethome", languageUtils.getString("sethome_function"), "sethome");
            CommandRegister.registerCommand(plugin, new DeleteHomeCommands(), "ultikits.tools.delhome", languageUtils.getString("delhome_function"), "delhome");
            CommandRegister.registerCommand(plugin, new HomeListCommands(), "ultikits.tools.homelist", languageUtils.getString("listhome_function"), "homelist");
            getServer().getPluginManager().registerEvents(new HomeListPageListener(), this);
        }
        if (this.getConfig().getBoolean("enable_white_list")) {
            CommandRegister.registerCommand(plugin, new WhitelistCommands(), "ultikits.tools.whitelist", languageUtils.getString("whitelist_function"), "wl");
            Bukkit.getPluginManager().registerEvents(new WhitelistListener(), this);
        }
        if (this.getConfig().getBoolean("enable_scoreboard")) {
            CommandRegister.registerCommand(plugin, new SbCommands(), "ultikits.tools.scoreboard", languageUtils.getString("sidebar_function"), "sb");
            new SideBarTask().runTaskTimerAsynchronously(this, 0, 20L);
        }
        if (this.getConfig().getBoolean("enable_lock")) {
            CommandRegister.registerCommand(plugin, new UnlockCommands(), "ultikits.tools.lock", languageUtils.getString("lock_chest_function"), "unlock", "ul");
            CommandRegister.registerCommand(plugin, new LockCommands(), "ultikits.tools.unlock", languageUtils.getString("unlock_chest_function"), "lock", "l");
            Bukkit.getPluginManager().registerEvents(new ChestLockListener(), this);
        }
        if (this.getConfig().getBoolean("enable_remote_chest")) {
            CommandRegister.registerCommand(plugin, new RemoteBagCommands(), "ultikits.tools.bag", languageUtils.getString("bag_function"), "bag", "b");
            CommandRegister.registerCommand(plugin, new RemoteBagConsoleCommands(), "ultikits.tools.admin", languageUtils.getString("bag_console_function"), "createbag");
            getServer().getPluginManager().registerEvents(new ChestPageListener(), this);
        }
        if (this.getConfig().getBoolean("enable_multiworlds")) {
            CommandRegister.registerCommand(plugin, new MultiWorldsCommands(), "ultikits.tools.mw", languageUtils.getString("multiworlds_function"), "mw");
            getServer().getPluginManager().registerEvents(new WorldsListListener(), this);
        }
        if (this.getConfig().getBoolean("enable_kits")) {
            CommandRegister.registerCommand(plugin, new KitsCommands(), "ultikits.tools.kits", languageUtils.getString("kits_function"), "kits");
            getServer().getPluginManager().registerEvents(new CreateKitsViewListener(), this);
            getServer().getPluginManager().registerEvents(new KitsPageListener(), this);
        }
        if (this.getConfig().getBoolean("enable_cleaner")) {
            CommandRegister.registerCommand(plugin, new CleanerCommands(), "ultikits.tools.clean", languageUtils.getString("cleaner_function"), "clean");
        }
//        if (this.getConfig().getBoolean("enable_permission")) {
//            CommandRegister.registerCommand(plugin, new PermissionCommands(), "ultikits.tools.permission", languageUtils.getString("permission_function"), "pers");
//            getServer().getPluginManager().registerEvents(new PermissionAddOnJoinListener(), this);
//        }
        if (this.getConfig().getBoolean("enable_tpa")) {
            CommandRegister.registerCommand(plugin, new TeleportCommands(), "ultikits.tools.tpa", languageUtils.getString("tpa_function"), "tpa");
            CommandRegister.registerCommand(plugin, new TpaHereCommands(), "ultikits.tools.tpa", languageUtils.getString("tpa_function"), "tpahere");
            getServer().getPluginManager().registerEvents(new TpaAcceptListener(), this);
        }
        if (this.getConfig().getBoolean("enable_warp")) {
            CommandRegister.registerCommand(plugin, new WarpCommands(), "ultikits.tools.warp", languageUtils.getString("warp_function"), "warp", "w");
            CommandRegister.registerCommand(plugin, new WarpCommands(), "ultikits.tools.warp", languageUtils.getString("warp_function"), "warps");
            CommandRegister.registerCommand(plugin, new WarpCommands(), "ultikits.tools.warp", languageUtils.getString("warp_function"), "delwarp");
            CommandRegister.registerCommand(plugin, new WarpCommands(), "ultikits.tools.warp", languageUtils.getString("warp_function"), "setwarp");
            getServer().getPluginManager().registerEvents(new WarpListener(), this);
        }
        if (this.getConfig().getBoolean("enable_back")) {
            getServer().getPluginManager().registerEvents(new BackListener(), this);
            CommandRegister.registerCommand(plugin, new BackCommands(), "ultikits.tools.back", languageUtils.getString("back_function"), "back");
        }
        if (this.getConfig().getBoolean("enable_spawn")) {
            CommandRegister.registerCommand(plugin, new SpawnCommands(), "ultikits.tools.back", languageUtils.getString("back_function"), "spawn");
            CommandRegister.registerCommand(plugin, new SpawnCommands(), "ultikits.tools.back", languageUtils.getString("back_function"), "setspawn");
        }
        if (this.getConfig().getBoolean("enable_random_tp")) {
            CommandRegister.registerCommand(plugin, new RandomTpCommands(), "ultikits.tools.randomtp", languageUtils.getString("random_tp_function"), "wild");
        }

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        if (getConfig().getBoolean("enable_chat")) {
            getServer().getPluginManager().registerEvents(new ChatListener(), this);
        }
        if (getConfig().getBoolean("enable_login")) {
            getServer().getPluginManager().registerEvents(new LoginListener(), this);
            getServer().getPluginManager().registerEvents(new LoginGUIListener(), this);
            getServer().getPluginManager().registerEvents(new ValidationPageListener(), this);
            LoginListener.checkPlayerAlreadyLogin();
            CommandRegister.registerCommand(plugin, new LoginRegisterCommands(), "ultikits.tools.login", languageUtils.getString("login_function"), "reg", "regs", "re");
        }
        if (getConfig().getBoolean("enable_death_punishment")) {
            getServer().getPluginManager().registerEvents(new DeathListener(), this);
        }

        //注册任务
        if (this.getConfig().getBoolean("enable_name_prefix")) {
            new NamePrefixSuffixTask().runTaskTimer(this, 0, 20L);
        }
        if (this.getConfig().getBoolean("enable_cleaner")) {
            new CleanerTask().runTaskTimer(this, 10 * 20L, 10 * 20L);
            new UnloadChunksTask().runTaskTimer(this, 0L, 60 * 20L);
        }
        if (getConfig().getBoolean("enable_pro")) {
            new ProCheckerTask().runTaskTimerAsynchronously(this, 12000L, 12000L);
        }

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("plugin_loaded"));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("author") + "wisdomme");

        //检查更新
        if (getConfig().getBoolean("enable_version_check")) {
            VersionChecker.runTask();
        }
    }

    @Override
    public void onDisable() {
        if (!isUltiCoreUpToDate){
            return;
        }
        if (getConfig().getBoolean("enable_login")) {
            for (String player : LoginListener.playerLoginStatus.keySet()) {
                if (Bukkit.getPlayerExact(player) != null) {
                    Player player1 = Bukkit.getPlayerExact(player);
                    assert player1 != null;
                    if (!getIsLogin(player1)) {
                        player1.kickPlayer(ChatColor.AQUA + "[UltiTools Login] " + languageUtils.getString("login_plugin_reloaded"));
                    }
                }
            }
            savePlayerLoginStatus();
        }
        if (this.getConfig().getBoolean("enable_scoreboard")) {
            clearScoreboards();
        }
//        ConfigController.saveConfigs();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("plugin_disabled"));
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

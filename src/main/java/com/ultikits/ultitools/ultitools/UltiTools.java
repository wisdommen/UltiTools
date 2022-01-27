package com.ultikits.ultitools.ultitools;

import com.ultikits.api.VersionWrapper;
import com.ultikits.checker.ProChecker;
import com.ultikits.main.UltiCoreAPI;
import com.ultikits.ultitools.checker.DependencyChecker;
import com.ultikits.ultitools.checker.PlayerlistChecker;
import com.ultikits.ultitools.checker.VersionChecker;
import com.ultikits.ultitools.commands.*;
import com.ultikits.ultitools.config.*;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.*;
import com.ultikits.ultitools.register.CommandRegister;
import com.ultikits.ultitools.register.PapiRegister;
import com.ultikits.ultitools.tasks.*;
import com.ultikits.ultitools.utils.*;
import com.ultikits.utils.DatabaseUtils;
import com.ultikits.utils.MessagesUtils;
import com.ultikits.utils.Metrics;
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
    public static boolean isDatabaseEnabled;
    public static DatabaseUtils databaseUtils;
    private static boolean isUltiCoreUpToDate;
    private ProChecker proChecker;
    private static final String banner = "\n" +
            "§b§n==================================================§f\n" +
            "§b§l   __  __ __ __   _  ______               __     §f\n" +
            "§b§l  / / / // // /_ (_)/_  __/____   ____   / /_____§f\n" +
            "§b§l / / / // // __// /  / /  / __ \\ / __ \\ / // ___/§f\n" +
            "§b§l/ /_/ // // /_ / /  / /  / /_/ // /_/ // /(__  ) §f\n" +
            "§b§l\\____//_/ \\__//_/  /_/   \\____/ \\____//_//____/  §f\n" +
            "§f                                                 §f\n" +
            "§b§n==================================================§f\n";

    @Override
    public void onEnable() {
        plugin = this;
        this.getServer().getConsoleSender().sendMessage(banner);
        isUltiCoreUpToDate = DependencyChecker.isUltiCoreUpToDate();
        if (!isUltiCoreUpToDate) {
            this.getServer().getConsoleSender().sendMessage(MessagesUtils.warning("The version of UltiCoreAPI is too old to enable UltiTools!"));
            this.getServer().getConsoleSender().sendMessage(MessagesUtils.warning("Use [/ulticore upgrade] to download the newest version of UltiCoreAPI!"));
            this.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        ultiCoreAPI = new UltiCoreAPI(this);
        isPAPILoaded = UltiCoreAPI.isPapiLoaded();
        Metrics metrics = new Metrics(this, 8652);
        metrics.addCustomChart(new Metrics.SimplePie("pro_user_count", () -> String.valueOf(UltiTools.getInstance().getConfig().getBoolean("enable_pro") && UltiTools.getInstance().getProChecker().getProStatus())));
        metrics.addCustomChart(new Metrics.AdvancedPie("function_used", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            for (String each : FunctionUtils.getAllFunctions()) {
                boolean enabled = getConfig().getBoolean(FunctionUtils.getFunctionCode(each));
                valueMap.put(each, enabled ? 1 : 0);
            }
            return valueMap;
        }));

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

        if (cusLang.equalsIgnoreCase("cn") || cusLang.equalsIgnoreCase("us")) {
            File langFile = new File(getDataFolder().getPath() + File.separator + "lang", language + ".yml");
            languageUtils = new LanguageUtils(YamlConfiguration.loadConfiguration(langFile));
        } else {
            File langFile = new File(getDataFolder().getPath() + File.separator + "lang", cusLang + ".yml");
            languageUtils = new LanguageUtils(YamlConfiguration.loadConfiguration(langFile));
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("using_customized_language"));
        }

        if (!new File(ConfigsEnum.LOBBY.toString()).exists()) {
            yaml.saveYamlFile(getDataFolder().getPath(), "lobby.yml", "lobby.yml");
        }
        if (!new File(ConfigsEnum.ANNOUNCEMENT.toString()).exists()) {
            yaml.saveYamlFile(getDataFolder().getPath(), "announcement.yml", language + "_announcement.yml");
        }
        if (!new File(ConfigsEnum.COMMANDALIAS.toString()).exists()) {
            yaml.saveYamlFile(getDataFolder().getPath(), "command-alias.yml", language + "_command-alias.yml");
        }
        if (!new File(ConfigsEnum.BANLIST.toString()).exists()) {
            yaml.saveYamlFile(getDataFolder().getPath(), "banlist.yml", "banlist.yml");
        }

        new PlayerlistChecker().playerlistNewChecker();                                                                 //playerlist.yml文件转换

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
        folders.add(new File(getDataFolder() + "/playerData" + "/playerlist"));
        folders.add(new File(getDataFolder() + "/InventoryBackupData"));

        makedirs(folders);

        Arrays.asList(
                new KitsConfig(),
                new CleanerConfig(),
/*权限组功能已弃用
                new GroupPermissionConfig(),
                new UserPermissionConfig(),
                new GlobuleGroupsConfig(),
*/
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
                new CustomerGUIConfig(),
                new TradeConfig(),
                new MOTDConfig(),
                new TabBarConfig()
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
                            proChecker = new ProChecker(getConfig().getString("pro_name"), getConfig().getString("pro_password"));
                            String res = proChecker.validatePro();
                            if (res.equals("Pro Version Activated!")) {
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + languageUtils.getString("pro_validated"));
                            } else {
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] " + languageUtils.getString("pro_validation_failed"));
                            }
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + res);
                        } catch (Exception e) {
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
        } else {
            new PapiRegister().register();
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
            for (Player player : Bukkit.getOnlinePlayers()) {
                ScoreBoardUtils.registerPlayer(player.getUniqueId());
            }
            CommandRegister.registerCommand(plugin, new SbCommands(), "ultikits.tools.scoreboard", languageUtils.getString("sidebar_function"), "sb");
            new SideBarTask().runTaskTimerAsynchronously(this, 0, 20L);
        }
        if (this.getConfig().getBoolean("enable_lock")) {
            CommandRegister.registerCommand(plugin, new UnlockCommands(), "ultikits.tools.chest.lock", languageUtils.getString("lock_chest_function"), "unlock", "ul");
            CommandRegister.registerCommand(plugin, new LockCommands(), "ultikits.tools.chest.unlock", languageUtils.getString("unlock_chest_function"), "lock", "l");
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
/*权限组功能已弃用
        if (this.getConfig().getBoolean("enable_permission")) {
            CommandRegister.registerCommand(plugin, new PermissionCommands(), "ultikits.tools.permission", languageUtils.getString("permission_function"), "pers");
            getServer().getPluginManager().registerEvents(new PermissionAddOnJoinListener(), this);
        }
*/
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
            CommandRegister.registerCommand(plugin, new RandomTpCommands(), "ultikits.tools.command.wild", languageUtils.getString("random_tp_function"), "wild");
        }
        if (this.getConfig().getBoolean("enable_fly_command")) {
            CommandRegister.registerCommand(plugin, new FlyCommands(), "ultikits.tools.command.fly", languageUtils.getString("fly_function"), "fly");
        }
        if (this.getConfig().getBoolean("enable_tpback_command")) {
            CommandRegister.registerCommand(plugin, new TpbackCommands(), "ultikits.tools.command.tpback,", languageUtils.getString("tpback_function"), "tpback");
            getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        }
        if (this.getConfig().getBoolean("enable_lobby_command")) {
            CommandRegister.registerCommand(plugin, new LobbyCommands(), "ultikits.tools.back", languageUtils.getString("back_function"), "setlobby");
            CommandRegister.registerCommand(plugin, new LobbyCommands(), "ultikits.tools.back", languageUtils.getString("back_function"), "lobby");
        }
        if (this.getConfig().getBoolean("enable_invsee_command")) {
            CommandRegister.registerCommand(plugin, new InvseeCommands(), "ultikits.tools.admin", languageUtils.getString("invsee_function"), "invsee");
        }
        if (this.getConfig().getBoolean("enable_enderChest_see_command")) {
            CommandRegister.registerCommand(plugin, new InvseeCommands(), "ultikits.tools.admin", languageUtils.getString("enderChest_see_function"), "endersee");
        }
        CommandRegister.registerCommand(plugin, new HealCommands(), "ultikits.tools.command.heal", languageUtils.getString("heal_function"), "heal", "h");
        CommandRegister.registerCommand(plugin, new GameModeCommands(), "ultikits.tools.command.gm", "gamemode",  "gm");
        CommandRegister.registerCommand(plugin, new SpeedCommands(), "ultikits.tools.command.speed", "speed", "speed");

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        //防止放入物品至自定义GUI
        Bukkit.getPluginManager().registerEvents(new CustomGUIProtectListener(), this);

        if (getConfig().getBoolean("enable_chat")) {
            getServer().getPluginManager().registerEvents(new ChatListener(), this);
        }
        if (getConfig().getBoolean("enable_login")) {
            getServer().getPluginManager().registerEvents(new LoginListener(), this);
            getServer().getPluginManager().registerEvents(new LoginGUIListener(), this);
            getServer().getPluginManager().registerEvents(new ValidationPageListener(), this);
            LoginListener.checkPlayerAlreadyLogin();
            CommandRegister.registerCommand(plugin, new LoginRegisterCommands(), "ultikits.tools.login", languageUtils.getString("login_function"), "reg", "regs", "re");
            CommandRegister.registerCommand(plugin, new PasswordCommands(), "ultikits.tools.command.password", languageUtils.getString("password_function"), "password", "pwd");
        }
        if (getConfig().getBoolean("enable_death_punishment")) {
            getServer().getPluginManager().registerEvents(new DeathListener(), this);
        }
        if (getConfig().getBoolean("enable_social_system")) {
            CommandRegister.registerCommand(plugin, new SocialSystemCommands(), "ultikits.tools.social", languageUtils.getString("friend_function"), "soc", "friends", "fri");
            getServer().getPluginManager().registerEvents(new FriendsApplyViewListener(), this);
            getServer().getPluginManager().registerEvents(new FriendsViewListener(), this);
        }
        if (getConfig().getBoolean("enable_trade")) {
            CommandRegister.registerCommand(plugin, new TradeCommands(), "ultikits.tools.trade", languageUtils.getString("trade_function"), "t", "trade");
            getServer().getPluginManager().registerEvents(new TradeListener(), this);
        }
        if (getConfig().getBoolean("enable_announcement")) {
            BroadcastTask.run();
        }
        if (getConfig().getBoolean("enable_motd_funcion")) {
            getServer().getPluginManager().registerEvents(new MOTDListener(), this);
        }
        if (getConfig().getBoolean("enable_command-alias_function")) {
            getServer().getPluginManager().registerEvents(new CommandListener(), this);
            for (String alia : new CommandListener().getCommandAliasList()) {
                CommandRegister.registerCommand(this, null, null, null, alia);
            }
        }
        if (getConfig().getBoolean("enable_inv_backup_function")) {
            CommandRegister.registerCommand(plugin, new InventoryBackupCommands(), "ultikits.tools.admin", "背包备份", "inv", "inventory");
            Bukkit.getServer().getPluginManager().registerEvents(new ItemClickListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new InventoryBackupViewListener(), this);
        }

        if (getConfig().getBoolean("enable_recall_command")) {
            CommandRegister.registerCommand(plugin, new RecallCommands(), "ultikits.tools.admin", "玩家召回", "recall");
        }


        //注册任务
        if (getConfig().getBoolean("enable_pro")) {
            new ProCheckerTask().runTaskTimerAsynchronously(this, 12000L, 12000L);
        }
        if (this.getConfig().getBoolean("enable_name_prefix")) {
            new NamePrefixSuffixTask().runTaskTimer(this, 0, 20L);
        }
        if (this.getConfig().getBoolean("enable_cleaner")) {
            new CleanerTask().runTaskTimer(this, 10 * 20L, 10 * 20L);
            new UnloadChunksTask().runTaskTimer(this, 0L, 60 * 20L);
        }
        if (getConfig().getBoolean("enable_ban_function")) {
            CommandRegister.registerCommand(this, new BanCommands(), "ultikits.tools.admin", UltiTools.languageUtils.getString("ban_function"), "ultiban");
            CommandRegister.registerCommand(this, new BanCommands(), "ultikits.tools.admin", UltiTools.languageUtils.getString("ban_function"), "ultibanip");
            CommandRegister.registerCommand(this, new BanCommands(), "ultikits.tools.admin", UltiTools.languageUtils.getString("ban_function"), "ultibanlist");
            Bukkit.getServer().getPluginManager().registerEvents(new BanListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new BanlistViewListener(), this);
            new BanTimeCheckerTask().startBanTimeCheckerTask();
        }

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("plugin_loaded"));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("author") + "wisdomme");

        //检查更新
        if (getConfig().getBoolean("enable_version_check")) {
            VersionChecker.runTask();
        }

        ChestLockUtils.transformOldData();                                                                              //chestData.yml文件转换
    }

    @Override
    public void onDisable() {
        if (!isUltiCoreUpToDate) {
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
            ScoreBoardUtils.clearScoreboards();
        }
//        ConfigController.saveConfigs();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("plugin_disabled"));
    }

    public static UltiTools getInstance() {
        return plugin;
    }

    public ProChecker getProChecker() {
        return proChecker;
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

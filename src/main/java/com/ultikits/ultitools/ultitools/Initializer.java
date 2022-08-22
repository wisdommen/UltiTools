package com.ultikits.ultitools.ultitools;

import com.ultikits.checker.ProChecker;
import com.ultikits.data.DatabaseConfig;
import com.ultikits.manager.HibernateManager;
import com.ultikits.ultitools.checker.DependencyChecker;
import com.ultikits.ultitools.config.*;
import com.ultikits.ultitools.dao.UserInfo;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.LoginListener;
import com.ultikits.ultitools.register.PapiRegister;
import com.ultikits.ultitools.register.RecipeRegister;
import com.ultikits.ultitools.services.ScoreBoardService;
import com.ultikits.ultitools.tasks.*;
import com.ultikits.ultitools.utils.FunctionUtils;
import com.ultikits.ultitools.utils.LanguageUtils;
import com.ultikits.utils.MessagesUtils;
import com.ultikits.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.hibernate.SessionFactory;

import java.io.File;
import java.util.*;

import static com.ultikits.ultitools.ultitools.UltiTools.*;

public class Initializer {

    private final Plugin plugin;
    private ProChecker proChecker;

    protected Initializer(Plugin plugin) {
        this.plugin = plugin;
    }

    protected void setLocalLanguage() {
        Locale defaultLocale = Locale.getDefault();
        List<String> langs = Arrays.asList("en", "zh");
        language = defaultLocale.getLanguage();
        if (!langs.contains(language)) language = "en";
    }

    protected void makedirs(List<File> folders) {
        for (File eachFolder : folders) if (!eachFolder.exists()) eachFolder.mkdirs();
    }

    protected void initMetrics(Metrics metrics) {
        metrics.addCustomChart(
                new Metrics.SimplePie(
                        "pro_user_count",
                        () -> String.valueOf(
                                UltiTools.getInstance().getConfig().getBoolean("enable_pro")
                                        && UltiTools.getInstance().getProChecker().getProStatus()
                        )
                )
        );
        metrics.addCustomChart(new Metrics.AdvancedPie("function_used", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            for (String each : FunctionUtils.getAllFunctions()) {
                boolean enabled = plugin.getConfig().getBoolean(FunctionUtils.getFunctionCode(each));
                valueMap.put(each, enabled ? 1 : 0);
            }
            return valueMap;
        }));
    }

    protected void initConfig(List<File> folders) {
        if (!new File(ConfigsEnum.LOBBY.toString()).exists()) yaml.saveYamlFile(plugin.getDataFolder().getPath(), "lobby.yml", "lobby.yml");
        if (!new File(ConfigsEnum.ANNOUNCEMENT.toString()).exists()) yaml.saveYamlFile(plugin.getDataFolder().getPath(), "announcement.yml", language + "_announcement.yml");
        if (!new File(ConfigsEnum.COMMANDALIAS.toString()).exists()) yaml.saveYamlFile(plugin.getDataFolder().getPath(), "command-alias.yml", language + "_command-alias.yml");
        if (!new File(ConfigsEnum.BANLIST.toString()).exists()) yaml.saveYamlFile(plugin.getDataFolder().getPath(), "banlist.yml", "banlist.yml");
        folders.add(new File(plugin.getDataFolder().getPath()));
        folders.add(new File(plugin.getDataFolder() + "/playerData"));
        folders.add(new File(plugin.getDataFolder() + "/chestData"));
        folders.add(new File(plugin.getDataFolder() + "/loginData"));
        folders.add(new File(plugin.getDataFolder() + "/emailData"));
        folders.add(new File(plugin.getDataFolder() + "/permission"));
        folders.add(new File(plugin.getDataFolder() + "/sidebar"));
        folders.add(new File(plugin.getDataFolder() + "/kitData"));
        folders.add(new File(plugin.getDataFolder() + "/warps"));
        folders.add(new File(plugin.getDataFolder() + "/playerData" + "/playerlist"));
        folders.add(new File(plugin.getDataFolder() + "/InventoryBackupData"));
        makedirs(folders);
/*权限组功能已弃用
        new GroupPermissionConfig(),
        new UserPermissionConfig(),
        new GlobuleGroupsConfig(),
*/
        new KitsConfig();
        new CleanerConfig();
        new LoginConfig();
        new JoinWelcomeConfig();
        new SideBarConfig();
        new SideBarDataConfig();
        new ChestLockConfig();
        new HomeConfig();
        new ChestDataConfig();
        new MultiworldsConfig();
        new DeathPunishConfig();
        new MainConfig();
        new WhiteListConfig();
        new BagConfig();
        new ChatConfig();
        new CustomerGUIConfig();
        new TradeConfig();
        new MOTDConfig();
        new TabBarConfig();
        new RecipeConfig();
    }

    protected SessionFactory initDataBase() {
        isDatabaseEnabled = plugin.getConfig().getBoolean("enableDataBase");
        if (isDatabaseEnabled) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("initializing_database"));
            String ip       = plugin.getConfig().getString("host");
            String port     = plugin.getConfig().getString("port");
            String username = plugin.getConfig().getString("username");
            String password = plugin.getConfig().getString("password");
            String database = plugin.getConfig().getString("database");
            DatabaseConfig config = DatabaseConfig.builder()
                    .ip(ip)
                    .port(port)
                    .username(username)
                    .password(password)
                    .database(database)
                    .build();
            boolean register = HibernateManager.register(getInstance(), config, UserInfo.class);
            if (register){
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("database_connected"));
                return HibernateManager.getSessionFactory(UltiTools.getInstance());
            }else {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("database_connect_failed"));
                isDatabaseEnabled = false;
            }
        }
        return null;
    }

    protected void initLanguage(File folder, File config_file) {
        setLocalLanguage();
        if (!folder.exists() || !config_file.exists()) {
            folder.mkdirs();
            yaml.saveYamlFile(plugin.getDataFolder().getPath(), "config.yml", language + "_config.yml");
        }
        language = Objects.requireNonNull(plugin.getConfig().getString("language")).split("_")[0];
        String cusLang = Objects.requireNonNull(plugin.getConfig().getString("language")).split("_")[1];
        yaml.saveYamlFile(plugin.getDataFolder().getPath() + File.separator + "lang", language + ".yml", language + ".yml", true);

        if (cusLang.equalsIgnoreCase("cn") || cusLang.equalsIgnoreCase("us")) {
            File langFile = new File(plugin.getDataFolder().getPath() + File.separator + "lang", language + ".yml");
            languageUtils = new LanguageUtils(YamlConfiguration.loadConfiguration(langFile));
        } else {
            File langFile = new File(plugin.getDataFolder().getPath() + File.separator + "lang", cusLang + ".yml");
            languageUtils = new LanguageUtils(YamlConfiguration.loadConfiguration(langFile));
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("using_customized_language"));
        }
    }

    protected ProChecker initPro() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfig().getBoolean("enable_pro")) return;
                try {
                    proChecker = new ProChecker(plugin.getConfig().getString("pro_name"), plugin.getConfig().getString("pro_password"));
                    String res = proChecker.validatePro();
                    if (res.equals("Pro Version Activated!")) {
                        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + languageUtils.getString("pro_validated"));
                    } else {
                        plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] " + languageUtils.getString("pro_validation_failed"));
                    }
                    plugin.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + res);
                } catch (Exception e) {
                    plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] " + languageUtils.getString("pro_validation_failed"));
                }
            }
        }.runTaskAsynchronously(plugin);
        return proChecker;
    }

    protected void initPAPI() {
        if (!isPAPILoaded) {
            plugin.getLogger().warning("[UltiTools] " + languageUtils.getString("papi_not_found"));
            if (plugin.getServer().getPluginManager().getPlugin("UltiLevel") == null) plugin.getLogger().warning("[UltiTools] " + languageUtils.getString("ultilevel_not_found"));
        } else {
            new PapiRegister().register();
        }
    }

    protected void initWorld() {
        if (plugin.getConfig().getBoolean("enable_multiworlds")) return;
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("loading_worlds"));
        File worldFile = new File(plugin.getDataFolder(), "worlds.yml");
        YamlConfiguration worldConfig = YamlConfiguration.loadConfiguration(worldFile);
        List<String> worlds = worldConfig.getStringList("worlds");
        for (String eachWorld : worlds) plugin.getServer().createWorld(new WorldCreator(eachWorld));
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + languageUtils.getString("worlds_load_successfully"));
    }

    protected void sendBanner() {
        String banner = "\n" +
                "§b§n==================================================§f\n" +
                "§b§l   __  __ __ __   _  ______               __     §f\n" +
                "§b§l  / / / // // /_ (_)/_  __/____   ____   / /_____§f\n" +
                "§b§l / / / // // __// /  / /  / __ \\ / __ \\ / // ___/§f\n" +
                "§b§l/ /_/ // // /_ / /  / /  / /_/ // /_/ // /(__  ) §f\n" +
                "§b§l\\____//_/ \\__//_/  /_/   \\____/ \\____//_//____/  §f\n" +
                "§f                                                 §f\n" +
                "§b§n==================================================§f\n";
        plugin.getServer().getConsoleSender().sendMessage(banner);
    }

    protected boolean checkCore() {
        boolean isUltiCoreUpToDate = DependencyChecker.isUltiCoreUpToDate();
        if (isUltiCoreUpToDate) return true;
        plugin.getServer().getConsoleSender().sendMessage(MessagesUtils.warning("The version of UltiCoreAPI is too old to enable UltiTools!"));
        plugin.getServer().getConsoleSender().sendMessage(MessagesUtils.warning("Use [/ulticore upgrade] to download the newest version of UltiCoreAPI!"));
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        return false;
    }

    protected void runTasks() {
        if (plugin.getConfig().getBoolean("enable_scoreboard")) {
            for (Player player : Bukkit.getOnlinePlayers()) ScoreBoardService.registerPlayer(player.getUniqueId());
            new SideBarTask().runTaskTimerAsynchronously(plugin, 0, 20L);
        }
        if (plugin.getConfig().getBoolean("enable_custom_recipe")) RecipeRegister.initRecipe();
        if (plugin.getConfig().getBoolean("enable_login")) LoginListener.checkPlayerAlreadyLogin();
        if (plugin.getConfig().getBoolean("enable_announcement")) BroadcastTask.run();
        if (plugin.getConfig().getBoolean("enable_pro")) new ProCheckerTask().runTaskTimerAsynchronously(plugin, 12000L, 12000L);
        if (plugin.getConfig().getBoolean("enable_name_prefix")) new NamePrefixSuffixTask().runTaskTimer(plugin, 0, 20L);
        if (plugin.getConfig().getBoolean("enable_cleaner")) {
            new CleanerTask().runTaskTimer(plugin, 10 * 20L, 10 * 20L);
            new UnloadChunksTask().runTaskTimer(plugin, 0L, 60 * 20L);
        }
        if (plugin.getConfig().getBoolean("enable_ban_function")) new BanTimeCheckerTask().startBanTimeCheckerTask();
    }
}

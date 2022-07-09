package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FunctionUtils {

    private static final Map<String, String>       functionsMap = new LinkedHashMap<>();
    private static final Map<String, String>       listenersMap = new LinkedHashMap<>();
    private static final Map<String, List<String>> commandsMap  = new LinkedHashMap<>();

    static {
        functionsMap.put("email", "enable_email");
        functionsMap.put("login", "enable_login");
        functionsMap.put("home", "enable_home");
        functionsMap.put("whitelist", "enable_white_list");
        functionsMap.put("sidebar", "enable_scoreboard");
        functionsMap.put("chestlock", "enable_lock");
        functionsMap.put("remote-bag", "enable_remote_chest");
        functionsMap.put("multiworlds", "enable_multiworlds");
        functionsMap.put("kits", "enable_kits");
        functionsMap.put("cleaner", "enable_cleaner");
        functionsMap.put("permission", "enable_permission");
        functionsMap.put("join-welcome", "enable_onjoin");
        functionsMap.put("armor-monitor", "enable_armor_check");
        functionsMap.put("chat-prefix", "enable_chat");
        functionsMap.put("name-prefix", "enable_name_prefix");
        functionsMap.put("death-punishment", "enable_death_punishment");
        functionsMap.put("tpa", "enable_tpa");
        functionsMap.put("warp", "enable_wrap");
        functionsMap.put("papi", "enable_PAPI");
        functionsMap.put("social-system", "enable_social_system");
        functionsMap.put("random-tp", "enable_random_tp");
        functionsMap.put("auto-version-check", "enable_version_check");
        functionsMap.put("auto-update", "enable_auto_update");

        listenersMap.put("EmailPageListener", "enable_email");
        listenersMap.put("HomeListPageListener", "enable_home");
        listenersMap.put("WhitelistListener", "enable_white_list");
        listenersMap.put("ChestLockListener", "enable_lock");
        listenersMap.put("ChestPageListener", "enable_remote_chest");
        listenersMap.put("WorldsListListener", "enable_multiworlds");
        listenersMap.put("MultiWorldListener", "enable_multiworlds");
        listenersMap.put("CreateKitsViewListener", "enable_kits");
        listenersMap.put("KitsPageListener", "enable_kits");
        listenersMap.put("TpaAcceptListener", "enable_tpa");
        listenersMap.put("WarpListener", "enable_warp");
        listenersMap.put("BackListener", "enable_back");
        listenersMap.put("TeleportListener", "enable_tpback_command");
        listenersMap.put("JoinListener", "core");
        listenersMap.put("CustomGUIProtectListener", "core");
        listenersMap.put("ChatListener", "enable_chat");
        listenersMap.put("LoginListener", "enable_login");
        listenersMap.put("LoginGUIListener", "enable_login");
        listenersMap.put("ValidationPageListener", "enable_login");
        listenersMap.put("DeathListener", "enable_death_punishment");
        listenersMap.put("FriendsApplyViewListener", "enable_social_system");
        listenersMap.put("FriendsViewListener", "enable_social_system");
        listenersMap.put("TradeListener", "enable_trade");
        listenersMap.put("MOTDListener", "enable_login");
        listenersMap.put("ItemClickListener", "enable_inv_backup_function");
        listenersMap.put("InventoryBackupViewListener", "enable_inv_backup_function");
        listenersMap.put("BanListener", "enable_ban_function");
        listenersMap.put("BanlistViewListener", "enable_ban_function");
        listenersMap.put("CommandListener", "enable_command-alias_function");

        commandsMap.put("EmailCommands", Arrays.asList("enable_email", "ultikits.tools.email", "email_function", "email,ultimail,mail,mails"));
        commandsMap.put("HomeCommands", Arrays.asList("enable_home", "ultikits.tools.home", "home_function", "home,h"));
        commandsMap.put("SetHomeCommands", Arrays.asList("enable_home", "ultikits.tools.sethome", "sethome_function", "sethome"));
        commandsMap.put("DeleteHomeCommands", Arrays.asList("enable_home", "ultikits.tools.delhome", "delhome_function", "delhome"));
        commandsMap.put("HomeListCommands", Arrays.asList("enable_home", "ultikits.tools.homelist", "listhome_function", "homelist"));
        commandsMap.put("WhitelistCommands", Arrays.asList("enable_white_list", "ultikits.tools.whitelist", "whitelist_function", "wl"));
        commandsMap.put("SbCommands", Arrays.asList("enable_scoreboard", "ultikits.tools.scoreboard", "sidebar_function", "sb"));
        commandsMap.put("UnlockCommands", Arrays.asList("enable_lock", "ultikits.tools.chest.unlock", "unlock_chest_function", "unlock,ul"));
        commandsMap.put("LockCommands", Arrays.asList("enable_lock", "ultikits.tools.chest.lock", "lock_chest_function", "lock,l"));
        commandsMap.put("RemoteBagCommands", Arrays.asList("enable_remote_chest", "ultikits.tools.bag", "bag_function", "bag,b"));
        commandsMap.put("RemoteBagConsoleCommands", Arrays.asList("enable_remote_chest", "ultikits.tools.admin", "bag_console_function", "createbag"));
        commandsMap.put("MultiWorldsCommands", Arrays.asList("enable_multiworlds", "ultikits.tools.mw", "multiworlds_function", "mw"));
        commandsMap.put("KitsCommands", Arrays.asList("enable_kits", "ultikits.tools.kits", "kits_function", "kits"));
        commandsMap.put("CleanerCommands", Arrays.asList("enable_cleaner", "ultikits.tools.clean", "cleaner_function", "clean"));
        commandsMap.put("TeleportCommands", Arrays.asList("enable_tpa", "ultikits.tools.tpa", "tpa_function", "tpa"));
        commandsMap.put("TpaHereCommands", Arrays.asList("enable_tpa", "ultikits.tools.tpa", "tpa_function", "tpahere"));
        commandsMap.put("WarpCommands", Arrays.asList("enable_warp", "enable_warp", "ultikits.tools.warp", "warp_function", "warp,w,warps,delwarp,setwarp"));
        commandsMap.put("BackCommands", Arrays.asList("enable_back", "ultikits.tools.back", "back_function", "spawn,setspawn"));
        commandsMap.put("SpawnCommands", Arrays.asList("enable_spawn", "ultikits.tools.back", "back_function", "email,ultimail,mail,mails"));
        commandsMap.put("RandomTpCommands", Arrays.asList("enable_random_tp", "ultikits.tools.command.wild", "random_tp_function", "wild"));
        commandsMap.put("FlyCommands", Arrays.asList("enable_fly_command", "ultikits.tools.command.fly", "fly_function", "fly"));
        commandsMap.put("TpbackCommands", Arrays.asList("enable_tpback_command", "ultikits.tools.command.tpback", "tpback_function", "tpback"));
        commandsMap.put("LobbyCommands", Arrays.asList("enable_lobby_command", "ultikits.tools.back", "back_function", "setlobby,lobby"));
        commandsMap.put("InvseeCommands", Arrays.asList("enable_invsee_command", "ultikits.tools.admin", "enderChest_see_function", "endersee"));
        commandsMap.put("HealCommands", Arrays.asList("core", "ultikits.tools.command.heal", "heal_function", "heal,h"));
        commandsMap.put("GameModeCommands", Arrays.asList("core", "ultikits.tools.command.gm", "gamemode", "gm"));
        commandsMap.put("SpeedCommands", Arrays.asList("core", "ultikits.tools.command.speed", "speed", "speed"));
        commandsMap.put("LoginRegisterCommands", Arrays.asList("enable_login", "ultikits.tools.login", "login_function", "reg,regs,re"));
        commandsMap.put("PasswordCommands", Arrays.asList("enable_login", "ultikits.tools.command.password", "password_function", "password,pwd"));
        commandsMap.put("SocialSystemCommands", Arrays.asList("enable_social_system", "ultikits.tools.social", "friend_function", "soc,friends,fri"));
        commandsMap.put("TradeCommands", Arrays.asList("enable_trade", "ultikits.tools.trade", "trade_function", "t,trade"));
        commandsMap.put("InventoryBackupCommands", Arrays.asList("enable_inv_backup_function", "ultikits.tools.admin", "bag_backup_function", "recall"));
        commandsMap.put("RecallCommands", Arrays.asList("enable_recall_command", "ultikits.tools.email", "recall_functionn", "email,ultimail,mail,mails"));
        commandsMap.put("BanCommands", Arrays.asList("enable_ban_function", "ultikits.tools.admin", "ban_function", "ultiban,ultibanip,ultibanlist"));
    }

    private FunctionUtils() {
    }

    public static void functionSwitch(String function, boolean isEnable){
        if (UltiTools.getInstance().getConfig().get(functionsMap.get(function))==null){
            System.out.println(function+" not found");
            return;
        }
        File file = new File(UltiTools.getInstance().getDataFolder(),"config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(functionsMap.get(function), isEnable);
        try {
            config.save(file);
            System.out.println("saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllFunctions(){
        return new ArrayList<>(functionsMap.keySet());
    }

    public static String getFunctionCode(String name){
        return functionsMap.get(name);
    }

    public static Map<String, String> getAllListeners() {
        return listenersMap;
    }

    public static Map<String, List<String>> getAllCommands() {
        return commandsMap;
    }
}

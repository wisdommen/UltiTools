package com.ultikits.ultitools.services;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.tasks.AtTask;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TextFilterUtils;
import com.ultikits.utils.MessagesUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ultikits.enums.Sounds.BLOCK_NOTE_BLOCK_BELL;

public class ChatService {

    public static final List<String> AtCD = new ArrayList<>();

    private static final List<String> ultilevelStrings = Arrays.asList(
            "%ul_level%",
            "%ul_job%",
            "%ul_exp%",
            "%ul_mp%",
            "%ul_max_mp%",
            "%ul_max_exp%",
            "%ul_health%",
            "%ul_max_health%",
            "%ul_q_cd%",
            "%ul_w_cd%",
            "%ul_e_cd%",
            "%ul_r_cd%"
    );

    public static List<String> splitMessage(String message) {
        Matcher matcher = Pattern.compile("\\[(\\S+?)]\\((\\S+?)\\)|\\{(\\S+?):(\\S+?)}|(.)").matcher(message);
        List<String> parts = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()) {
            String string = matcher.group();
            if (string.length() == 1) {
                stringBuilder.append(string);
            } else {
                String part = stringBuilder.toString();
                if (!part.equals("")) {
                    parts.add(part);
                    stringBuilder = new StringBuilder();
                }
                parts.add(string);
            }
        }
        return parts.isEmpty() ? Collections.singletonList(message) : parts;
    }

    public static List<BaseComponent[]> convertMessage(List<String> SplitMessage) {
        List<BaseComponent[]> ConvertedMessage = new ArrayList<>();
        ComponentBuilder componentBuilder;
        for (String part : SplitMessage) {
            if (part.startsWith("{") && part.endsWith("}")) {
                String content = part.substring(1, part.indexOf('}'));
                String token = content.split(":")[0];
                switch (token) {
                    case "LINK" :
                        componentBuilder
                                = new ComponentBuilder(content.replaceFirst("LINK:", "")).underlined(true).bold(true)
                                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, content.replaceFirst("LINK:", "")))
                                .event(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(ChatColor.AQUA + "点击打开链接: " + content.replaceFirst("LINK:", "")).underlined(true).bold(true).create()
                                ));
                        ConvertedMessage.add(componentBuilder.create());
                        break;
                    case "COMMAND" :
                        componentBuilder
                                = new ComponentBuilder(" [ " + content.replaceFirst("COMMAND:", "") + " ] ").bold(true)
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, content.replaceFirst("COMMAND:", "")))
                                .event(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(ChatColor.AQUA + "点击执行命令: " + content.replaceFirst("COMMAND:", "")).bold(true).create()
                                ));
                        ConvertedMessage.add(componentBuilder.create());
                        break;
                    case "TEXT" :
                        componentBuilder
                                = new ComponentBuilder(ChatColor.GOLD + " [鼠标悬浮来显示更多信息] ").bold(true)
                                .event(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(content.replaceFirst("TEXT:", "")).create()
                                ));
                        ConvertedMessage.add(componentBuilder.create());
                        break;
                    default:
                        componentBuilder = new ComponentBuilder(part);
                        ConvertedMessage.add(componentBuilder.create());
                        break;
                }
            } else if (part.startsWith("[") && part.endsWith(")")) {
                String prefix = part.split("]\\(")[0].substring(1);
                String suffix = part.split("]\\(")[1].replaceFirst(".$", "");
                if (!(prefix.contains(":") && !prefix.startsWith(":") && !prefix.endsWith(":"))) {
                    componentBuilder = new ComponentBuilder(part);
                    ConvertedMessage.add(componentBuilder.create());
                    continue;
                }
                String token = prefix.split(":")[0];
                switch (token) {
                    case "LINK" :
                        componentBuilder
                                = new ComponentBuilder("[" + prefix.split(":")[1] + "]").bold(true)
                                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, suffix))
                                .event(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(ChatColor.AQUA + "点击打开链接: " + suffix).create()
                                ));
                        ConvertedMessage.add(componentBuilder.create());
                        break;
                    case "COMMAND" :
                        componentBuilder
                                = new ComponentBuilder("[" + prefix.split(":")[1] + "]").bold(true)
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, suffix))
                                .event(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(ChatColor.AQUA + "点击执行命令: " + suffix).create()
                                ));
                        ConvertedMessage.add(componentBuilder.create());
                        break;
                    case "TEXT" :
                        componentBuilder
                                = new ComponentBuilder("[" + prefix.split(":")[1] + "]").bold(true)
                                .event(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(suffix).create()
                                ));
                        ConvertedMessage.add(componentBuilder.create());
                        break;
                    default:
                        componentBuilder = new ComponentBuilder(part);
                        ConvertedMessage.add(componentBuilder.create());
                        break;
                }
            } else {
                componentBuilder = new ComponentBuilder(part);
                ConvertedMessage.add(componentBuilder.create());
            }
        }
        return ConvertedMessage;
    }

    public static BaseComponent[] mergeMessages(List<BaseComponent[]> ConvertedMessage) {
        ComponentBuilder mergedMessage = new ComponentBuilder();
        for (BaseComponent[] message : ConvertedMessage) mergedMessage.append(message);
        return mergedMessage.create();
    }

    public static BaseComponent[] compileMessage(String RawMessage) {
        return mergeMessages(
                convertMessage(
                        splitMessage(RawMessage)
                )
        );
    }

    public static BaseComponent[] compilePrefix(Player player) {
        String prefixes_str = ConfigController.getConfig("chat").getString("chat_prefix").replaceAll("%player_name%", player.getName()).replaceAll("%player_world%", player.getLocation().getWorld().getName()).replaceAll("&", "§");
        prefixes_str = filterUltiLevelVariable(prefixes_str);
        String papiMassage = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, prefixes_str));
        String message = papiMassage + org.bukkit.ChatColor.WHITE + " ";
        return new ComponentBuilder(message.replaceAll("&", "§")).create();
    }

    public static void handleAutoReply(String RawMessage) {
        if (!UltiTools.getInstance().getConfig().getBoolean("enable_pro")) return;
        if (ConfigController.getConfig("config").getBoolean("enable_auto-reply") && UltiTools.getInstance().getProChecker().getProStatus()) {
            String message = RawMessage.replace(" ", "_");
            File file = new File(ConfigsEnum.CHAT.toString());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            Set<String> keys = config.getConfigurationSection("auto-reply").getKeys(false);
            String bestMatch = null;
            for (String each : keys) {
                if (!message.contains(each)) continue;
                if (bestMatch != null) {
                    if (bestMatch.length() < each.length()) bestMatch = each;
                } else {
                    bestMatch = each;
                }
            }
            String reply = config.getString("auto-reply."+bestMatch);
            if (reply != null) Bukkit.broadcastMessage(reply.replaceAll("&", "§"));
        }
    }

    public static void HandleAtNotification (Player player,Player sender) {
        player.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_atted")).replaceAll("%player%",sender.getName()));
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(BLOCK_NOTE_BLOCK_BELL), 10, 1);
        new AtTask(player,UltiTools.languageUtils.getString("chat_atted").replaceAll("%player%",sender.getName())).runTaskTimerAsynchronously(UltiTools.getInstance(),0,2L);
    }

    public static void filterAt(AsyncPlayerChatEvent e) {
        if (ConfigController.getConfig("config").getBoolean("enable_chat_att")) {
            String msg = e.getMessage();
            Player sender = e.getPlayer();
            if(msg.contains("@")) {
                if(msg.toLowerCase().contains("@" + UltiTools.languageUtils.getString("chat_at_all")) || msg.toLowerCase().contains("@ " + UltiTools.languageUtils.getString("chat_at_all"))) {
                    if(sender.hasPermission("ultikits.tools.atall") || sender.isOp() || sender.hasPermission("ultitools.tools.admin")) {
                        String msg0 = msg.replace("@", org.bukkit.ChatColor.DARK_GREEN + "@" + org.bukkit.ChatColor.RESET);
                        sender.sendMessage(UltiTools.languageUtils.getString("chat_at_you_at_all"));
                        e.setMessage(msg0.replace(UltiTools.languageUtils.getString("chat_at_all"), org.bukkit.ChatColor.DARK_GREEN + "" + org.bukkit.ChatColor.BOLD + UltiTools.languageUtils.getString("chat_at_all") + org.bukkit.ChatColor.RESET));
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            HandleAtNotification(player,sender);
                        }
                    } else {
                        sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
                        e.setCancelled(true);
                    }
                    return;
                }
                //被@的玩家的列表
                List<Player> atedPlayer = new ArrayList();
                int sum = 0;
                for(Player player : Bukkit.getOnlinePlayers()) {
                    //无视大小写比较
                    if(msg.toLowerCase().contains("@" + player.getName().toLowerCase()) || msg.toLowerCase().contains("@ " + player.getName().toLowerCase())) {
                        HandleAtNotification(player,sender);
                        atedPlayer.add(player);
                        sum++;
                    }
                }
                if(sum != 0) {
                    //@成功
                    String msg1 = msg.replace("@", org.bukkit.ChatColor.DARK_GREEN + "@" + org.bukkit.ChatColor.RESET);
                    msg1 += " ";
                    for(Player player: atedPlayer) {
                        String playerName = player.getName();
                        //校正大小写的玩家名字
                        String name = "";
                        int nameLength = playerName.length();
                        int msg1Length = msg1.length();
                        //读取到需要校正大小写的玩家名字
                        for(int i = 0; i < msg1Length - nameLength ;i++) {
                            name = msg1.substring(i, i + nameLength);
                            if (name.equalsIgnoreCase(playerName)) {
                                break;
                            }
                        }
                        msg1 = msg1.replace(name, org.bukkit.ChatColor.DARK_GREEN + "" + org.bukkit.ChatColor.BOLD + playerName + org.bukkit.ChatColor.RESET);
                    }
                    e.setMessage(msg1);
                    sender.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_at_success")).replaceAll("%num%", String.valueOf(sum)));
                } else {
                    //@不成功
                    sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("chat_at_error")));
                    String msg2 = msg.replace("@", org.bukkit.ChatColor.RED + "@" + org.bukkit.ChatColor.RESET);
                    e.setMessage(msg2);
                }
            }
        }
    }

    public static void filterChatColor(AsyncPlayerChatEvent event) {
        if(!ConfigController.getConfig("config").getBoolean("enable_chat_color")) return;
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        String Message = event.getMessage();
        if (Message.contains("&")) {
            if (player.hasPermission(new Permission("ultikits.tools.chatcolor"))) {
                event.setMessage(Message.replace("&", "§"));
            } else {
                player.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_color_nopermission_reply")));
            }
        }
    }

    public static String filterUltiLevelVariable(String string) {
        if (UltiTools.getInstance().getServer().getPluginManager().getPlugin("UltiLevel") == null) for (String each : ultilevelStrings) string = string.replace(each, "");
        return string;
    }

    public static void filterMessage(AsyncPlayerChatEvent event) {
        int mode = ConfigController.getConfig("chat").getInt("chat_filter.mode");
        String replacement = ConfigController.getConfig("chat").getString("chat_filter.replacement");
        TextFilterUtils textFilterUtils = new TextFilterUtils();
        switch (mode) {
            case 0:
                return;
            case 1:
                event.setMessage(textFilterUtils.replaceSensitiveWord(event.getMessage(), 1, replacement));
                break;
            case 2:
                if (textFilterUtils.isContainsSensitiveWord(event.getMessage(), 1)) {
                    BaseComponent[] prefix = compilePrefix(event.getPlayer());
                    filterAt(event);
                    if (event.isCancelled()) return;
                    filterChatColor(event);
                    BaseComponent[] FinalMessage = new ComponentBuilder("")
                            .append(prefix)
                            .append(compileMessage(event.getMessage())).create();
                    event.getPlayer().spigot().sendMessage(FinalMessage);
                    Bukkit.getConsoleSender().spigot().sendMessage(FinalMessage);
                    handleAutoReply(event.getMessage());
                    event.setCancelled(true);
                }
                break;
            case 3:
                if (textFilterUtils.isContainsSensitiveWord(event.getMessage(), 1)) {
                    event.getPlayer().sendMessage(UltiTools.languageUtils.getString("chat_forbidden"));
                    event.setCancelled(true);
                }
        }
    }
}
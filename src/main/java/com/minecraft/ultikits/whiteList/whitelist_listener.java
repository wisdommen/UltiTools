package com.minecraft.ultikits.whiteList;

import com.minecraft.economy.database.DataBase;
import com.minecraft.ultikits.ultitools.UltiTools;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import net.minecraft.server.v1_15_R1.PacketPlayOutLogin;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.util.List;

public class whitelist_listener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(UltiTools.getInstance().getDataFolder(), "whitelist.yml"));
        Player player = event.getPlayer();
        List<String> whitelist = config.getStringList("whitelist");

        PlayerConnection connection = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection;
//        PacketPlayOutLogin packet = new PacketPlayOutLogin(.a("[\"\",{\"text\":\"你还没有注册账号哦！\",\"color\":\"aqua\"},{\"text\":\"\n\"},{\"text\":\"请点击\",\"color\":\"aqua\"},{\"text\":\"这里\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://player.qianmo.space:88/index.html#\"}},{\"text\":\"注册账号！\",\"color\":\"aqua\"}]"));

        if (!UltiTools.getInstance().getConfig().getBoolean("enableDataBase")) {
            if (!whitelist.contains(player.getName()) && !player.isOp()) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + "你不在白名单上哦！\n近期有熊孩子出没，所以请加群903297604获取白名单资格");
            }
        }else {
        DataBase dataBase =  UltiTools.dataBase;
        dataBase.connect();
        if (dataBase.isExist(player.getName())) {
            if (dataBase.isExist(player.getName()) && Integer.parseInt((String) dataBase.getData(player.getName(), "active"))!=1){
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + "你不在白名单上哦！\n近期有熊孩子出没，所以请加群903297604获取白名单资格");
//                connection.sendPacket(packet);
            }
        }
    }
    }
}

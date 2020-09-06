package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.GroupManagerUtils;
import com.minecraft.ultikits.views.PermissionMainView;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.minecraft.ultikits.utils.MessagesUtils.info;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class PermissionCommands implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("ultikits.tools.admin")){
                player.sendMessage(warning("你没有权限！"));
                return true;
            }
            switch (args.length){
                case 0:
                    Inventory inventory = PermissionMainView.setUp();
                    player.openInventory(inventory);
                    return true;
                case 1:
                    if (args[0].equals("help")) sendHelpMessage(player);
                case 2:
                    switch (args[0]){
                        case "create":
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    GroupManagerUtils.createGroup(args[1]);
                                    player.sendMessage(info("已创建！"));
                                }
                            }.runTaskAsynchronously(UltiTools.getInstance());
                            return true;
                        case "delete":
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    GroupManagerUtils.deleteGroup(args[1]);
                                    player.sendMessage(warning("已删除！"));
                                }
                            }.runTaskAsynchronously(UltiTools.getInstance());
                            return true;
                        default:
                            return false;
                    }
                case 3:
                    switch (args[0]){
                        case "add":
                            Player addedPlayer = Bukkit.getPlayerExact(args[1]);
                            if (addedPlayer == null) {
                                player.sendMessage(warning("玩家不在线/不存在这个玩家！"));
                                return true;
                            }
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    GroupManagerUtils.addPlayerToGroup(addedPlayer, args[2]);
                                    player.sendMessage(info("已添加！"));
                                }
                            }.runTaskAsynchronously(UltiTools.getInstance());
                            return true;
                        case "remove":
                            Player removedPlayer = Bukkit.getPlayerExact(args[1]);
                            if (removedPlayer == null) {
                                player.sendMessage(warning("玩家不在线/不存在这个玩家！"));
                                return true;
                            }
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    GroupManagerUtils.removePlayerFromGroup(removedPlayer, args[2]);
                                    player.sendMessage(info("已移除！"));
                                }
                            }.runTaskAsynchronously(UltiTools.getInstance());
                            return true;
                        case "create":
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    GroupManagerUtils.createGroup(args[1], args[2]);
                                    player.sendMessage(info("已创建！"));
                                }
                            }.runTaskAsynchronously(UltiTools.getInstance());
                        default:
                            return false;
                    }
                case 4:
                    String permission = args[3];
                    switch (args[0]){
                        case "give":
                            switch (args[1]){
                                case "player":
                                    Player addedPlayer = Bukkit.getPlayerExact(args[2]);
                                    if (addedPlayer == null) {
                                        player.sendMessage(warning("玩家不在线/不存在这个玩家！"));
                                        return true;
                                    }
                                    GroupManagerUtils.addPlayerPermission(addedPlayer, permission);
                                    player.sendMessage(info("已添加！"));
                                    return true;
                                case "group":
                                    GroupManagerUtils.addGroupPermission(args[2], permission);
                                    player.sendMessage(info("已添加！"));
                                    return true;
                                default:
                                    return false;
                            }
                        case "take":
                            switch (args[1]){
                                case "player":
                                    Player addedPlayer = Bukkit.getPlayerExact(args[2]);
                                    if (addedPlayer == null) {
                                        player.sendMessage(warning("玩家不在线/不存在这个玩家！"));
                                        return true;
                                    }
                                    GroupManagerUtils.takePlayerPermission(addedPlayer, permission);
                                    player.sendMessage(info("已移除！"));
                                    return true;
                                case "group":
                                    GroupManagerUtils.takeGroupPermission(args[2], permission);
                                    player.sendMessage(info("已移除！"));
                                    return true;
                                default:
                                    return false;
                            }
                    }
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender.hasPermission("ultikits.tools.admin") || sender.isOp()){
            if (args.length==1){
                return Arrays.asList("help", "give", "take", "add", "remove", "create", "delete");
            }else if (args.length==2){
                switch (args[0]) {
                    case "give":
                    case "take":
                        return Arrays.asList("player", "group");
                    case "add":
                    case "remove":
                        return Collections.singletonList("[玩家名]");
                    case "create":
                    case "delete":
                        return Collections.singletonList("[权限组名]");
                    default:
                        return null;
                }
            }else if (args.length==3){
                switch (args[1]){
                    case "player":
                        return Collections.singletonList("[玩家名]");
                    case "group":
                        return Collections.singletonList("[权限组名]");
                    default:
                        switch (args[0]){
                            case "add":
                            case "remove":
                                return Collections.singletonList("[权限组名]");
                            case "create":
                                return Collections.singletonList("[继承的权限组]");
                            default:
                                return null;
                        }
                }
            }else if (args.length==4){
                if (args[0].equals("give")||args[0].equals("take")){
                    return Collections.singletonList("[权限]");
                }
                return null;
            }else {
                return null;
            }
        }
        return null;
    }

    private static void sendHelpMessage(CommandSender sender){
        sender.sendMessage(ChatColor.LIGHT_PURPLE+"-----权限组管理帮助-----");
        sender.sendMessage(ChatColor.AQUA+"/pers help"+ChatColor.GRAY+"  权限组帮助");
        sender.sendMessage(ChatColor.AQUA+"/pers give player [玩家名] [权限]"+ChatColor.GRAY+"  给玩家添加权限");
        sender.sendMessage(ChatColor.AQUA+"/pers give group [权限组名] [权限]"+ChatColor.GRAY+"  给权限组添加权限");
        sender.sendMessage(ChatColor.AQUA+"/pers take player [玩家名] [权限]"+ChatColor.GRAY+"  移除玩家的权限");
        sender.sendMessage(ChatColor.AQUA+"/pers take group [权限组名] [权限]"+ChatColor.GRAY+"  移除权限组的权限");
        sender.sendMessage(ChatColor.AQUA+"/pers add [玩家名] [权限组名]"+ChatColor.GRAY+"  将玩家添加到权限组");
        sender.sendMessage(ChatColor.AQUA+"/pers remove [玩家名] [权限组名]"+ChatColor.GRAY+"  将玩家移除权限组");
        sender.sendMessage(ChatColor.AQUA+"/pers create [权限组名]"+ChatColor.GRAY+"  创建权限组");
        sender.sendMessage(ChatColor.AQUA+"/pers create [权限组名] [继承的权限组]"+ChatColor.GRAY+"  创建权限组继承自另一个权限组");
        sender.sendMessage(ChatColor.AQUA+"/pers delete [权限组名]"+ChatColor.GRAY+"  删除权限组");
    }
}

package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TimeUtils;
import com.ultikits.ultitools.views.InventoryBackupView;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Shpries
 */
public class InventoryBackupCommands extends AbstractTabExecutor {
    Player sender;
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        sender = player;

        if(!player.isOp()) {
            return false;
        } else if(strings.length == 1) {
            //  /inv saveall
            if("saveall".equalsIgnoreCase(strings[0])) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            savePlayerInventoryData(p);
                        }
                        sender.sendMessage(MessagesUtils.info("inv_backup_save_success"));
                    }
                }.runTaskAsynchronously(UltiTools.getInstance());

                return true;
            }
            return false;
        } else if(strings.length == 2) {
            switch (strings[0].toLowerCase()) {
                case "save":
                    savePlayerInventoryData(Bukkit.getPlayer(strings[1]));
                    sender.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("inv_backup_save_success")));
                    break;
                // /inv load [player]
                case "load":
                    loadPlayerInventoryData(Bukkit.getPlayer(strings[1]));
                    sender.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("inv_backup_load_success")));
                    break;
                // /inv look [player]
                case "look":
                    if(!new File(ConfigsEnum.InventoryBackupData + "/" + strings[1] + ".yml").exists()) {
                        sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("inv_backup_null")));
                        return true;
                    } else {
                        sender.openInventory(InventoryBackupView.setUp(Bukkit.getPlayer(strings[1])));
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }


    //保存玩家物品栏
    private void savePlayerInventoryData(Player p) {
        if(!Bukkit.getOnlinePlayers().contains(p)) {
            MessagesUtils.warning(UltiTools.languageUtils.getString("player_not_online_or_not_exits"));
            return;
        }
        //该玩家文件信息
        Inventory i = p.getInventory();
        int InventorySize = i.getSize();
        File playerInventoryDataFile = new File(ConfigsEnum.InventoryBackupData + "/" + p.getName() + ".yml");
        YamlConfiguration playerInventoryDataConfig = YamlConfiguration.loadConfiguration(playerInventoryDataFile);
        //遍历 保存物品数据
        for(int num = 0;num < InventorySize;num++){
            ItemStack itemStack = i.getItem(num);
            if(!playerInventoryDataFile.exists()) {
                try {
                    playerInventoryDataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                playerInventoryDataFile.delete();
                try {
                    playerInventoryDataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(itemStack == null) {
                    playerInventoryDataConfig.set("InventoryData." + num," ");
                } else {
                    playerInventoryDataConfig.set("InventoryData." + num,itemStack);
                }
            }
        }
        //保存完成,补充数据
        playerInventoryDataConfig.set("InventoryInfo.LatestBackupTime", new TimeUtils().getTimeWithDate());
        try {
            playerInventoryDataConfig.save(playerInventoryDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载玩家物品栏
    private void loadPlayerInventoryData(Player p) {
        File playerInventoryDataFile = new File(ConfigsEnum.InventoryBackupData + "/" + p.getName() + ".yml");
        if(!playerInventoryDataFile.exists()) {
            sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("inv_backup_null")));
            return;
        }
        if(!Bukkit.getOnlinePlayers().contains(p)) {
            MessagesUtils.warning(UltiTools.languageUtils.getString("player_not_online_or_not_exits"));
            return;
        }
        YamlConfiguration playerInventoryDataConfig = YamlConfiguration.loadConfiguration(playerInventoryDataFile);
        Inventory i = Bukkit.getPlayer(p.getName()).getInventory();
        for(int num = 0; num < i.getSize(); num++) {
            if(playerInventoryDataConfig.get("InventoryData." + num).equals(" ")) {
                continue;
            } else {
                i.setItem(num,playerInventoryDataConfig.getItemStack("InventoryData." + num));
            }
        }
    }


    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!player.isOp()){
            return null;
        }
        if(strings.length == 1) {
            List<String> commands = new ArrayList();
            commands.add("saveall");
            commands.add("save");
            commands.add("look");
            commands.add("load");
            return commands;
        }
        if(strings.length == 2) {
            List<String> players = new ArrayList();
            for(Player p : Bukkit.getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        }
        return null;
    }
}

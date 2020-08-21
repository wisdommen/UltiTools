package com.minecraft.ultikits.kits;

import com.minecraft.ultikits.abstractClass.AbstractPlayerCommandExecutor;
import com.minecraft.ultikits.config.ConfigsEnum;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.minecraft.ultikits.GUIs.GUISetup.inventoryMap;
import static com.minecraft.ultikits.GUIs.GUISetup.setKit;


public class KitsCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!"kits".equalsIgnoreCase(command.getName())){
            return false;
        }
        setKit(player);
        player.openInventory(inventoryMap.get(player.getName()+".kits").getInventory());
        return true;
    }

    public static void initFile(){
        File file = new File(ConfigsEnum.KIT.toString());
        if (file.exists()) {
            return;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("xinshou.item", "OAK_PLANKS");
        config.set("xinshou.reBuyable", false);
        config.set("xinshou.name", "新手礼包");
        config.set("xinshou.level", 1);
        config.set("xinshou.job", "全部");
        config.set("xinshou.description", "虽然是木头的，但是却很实用");
        config.set("xinshou.price", 0);
        List<String> list = Arrays.asList("WOODEN_PICKAXE", "WOODEN_AXE", "WOODEN_SHOVEL", "WOODEN_SWORD", "WOODEN_HOE");
        for (String item : list){
            config.set("xinshou.contain."+item+".quantity", 1);
        }
        List<String> playerCommands = new ArrayList<>();
        config.set("xinshou.playerCommands",playerCommands);
        List<String> console = Arrays.asList("say {PLAYER} 领取了新手礼包", "givemoney {PLAYER} 100");
        config.set("xinshou.consoleCommands", console);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

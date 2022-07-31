package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.enums.Colors;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

@EventListener(function = "social-system")
public class FriendsApplyViewListener extends PagesListener {

    /*
    处理玩家申请好友的GUI界面点击事件
     */
    @Override
    public CancelResult onItemClick(InventoryClickEvent inventoryClickEvent, Player player, InventoryManager inventoryManager, ItemStack itemStack) {
        if (!inventoryClickEvent.getView().getTitle().contains(UltiTools.languageUtils.getString("friend_apply"))) {
            return CancelResult.NONE;
        }
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> friendsApply = config.getStringList("friends_apply");
        String applier = inventoryClickEvent.getView().getTitle().replace(UltiTools.languageUtils.getString("friend_apply"), "");
        if (friendsApply.contains(applier)) {
            if (itemStack.getType() == UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GREEN).getType()) {
                player.performCommand("friends accept " + applier);
            } else if (itemStack.getType() == UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED).getType()) {
                player.performCommand("friends reject " + applier);
            }
        } else {
            player.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("friend_not_applied"), ChatColor.YELLOW + applier + ChatColor.RED));
        }
        player.closeInventory();
        return CancelResult.TRUE;
    }
}

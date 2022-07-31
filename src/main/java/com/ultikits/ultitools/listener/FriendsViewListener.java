package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@EventListener(function = "social-system")
public class FriendsViewListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent inventoryClickEvent, Player player, InventoryManager inventoryManager, ItemStack itemStack) {
        if (!inventoryClickEvent.getView().getTitle().contains(UltiTools.languageUtils.getString("friend_list") + " - " + player.getName())) {
            return CancelResult.NONE;
        }
        String friend = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
        Player clickedFriend = Bukkit.getPlayer(friend);
        if (clickedFriend == null) {
            return CancelResult.TRUE;
        }
        if (inventoryClickEvent.getClick() == ClickType.LEFT) {
            player.performCommand("tpa " + friend);
        } else if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
            player.closeInventory();
            TextComponent text = new TextComponent(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("friend_click_tell"), ChatColor.RED + friend + ChatColor.YELLOW));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + friend + " "));
            player.spigot().sendMessage(text);
        }
        return CancelResult.TRUE;
    }
}

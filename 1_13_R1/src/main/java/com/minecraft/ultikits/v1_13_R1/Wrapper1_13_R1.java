package com.minecraft.ultikits.v1_13_R1;

import com.minecraft.ultikits.api.VersionWrapper;
import com.minecraft.ultikits.enums.Colors;
import com.minecraft.ultikits.enums.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Wrapper1_13_R1 implements VersionWrapper {

    public ItemStack getColoredPlaneGlass(Colors plane) {
        switch (plane){
            case RED:
                return new ItemStack(Material.RED_STAINED_GLASS_PANE);
            case BLUE:
                return new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
            case CYAN:
                return new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
            case GRAY:
                return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            case LIME:
                return new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            case PINK:
                return new ItemStack(Material.PINK_STAINED_GLASS_PANE);
            case BLACK:
                return new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            case BROWN:
                return new ItemStack(Material.BROWN_STAINED_GLASS_PANE);
            case GREEN:
                return new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            case WHITE:
                return new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            case ORANGE:
                return new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
            case PURPLE:
                return new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
            case YELLOW:
                return new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
            case MAGENTA:
                return new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
            case LIGHT_BLUE:
                return new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            case LIGHT_GRAY:
                return new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            default:
                return null;
        }
    }

    public ItemStack getSign() {
        return new ItemStack(Material.SIGN, 1);
    }

    public ItemStack getEndEye() {
        return new ItemStack(Material.ENDER_EYE, 1);
    }

    public ItemStack getEmailMaterial(boolean isRead) {
        if (isRead){
            return new ItemStack(Material.FILLED_MAP, 1);
        }else {
            return new ItemStack(Material.PAPER, 1);
        }
    }

    public ItemStack getHead(OfflinePlayer player) {
        if (player.isOp()) {
            return new ItemStack(Material.DRAGON_HEAD);
        } else {
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }

    public ItemStack getGrassBlock() {
        return new ItemStack(Material.GRASS_BLOCK, 1);
    }

    public Objective registerNewObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
        return scoreboard.registerNewObjective("侧边栏", "", ChatColor.DARK_AQUA + displayName);
    }

    public Sound getSound(Sounds sound) {
        switch (sound){
            case UI_TOAST_OUT:
                return Sound.UI_TOAST_OUT;
            case BLOCK_CHEST_OPEN:
                return Sound.BLOCK_CHEST_OPEN;
            case BLOCK_CHEST_LOCKED:
                return Sound.BLOCK_CHEST_LOCKED;
            case ITEM_BOOK_PAGE_TURN:
                return Sound.ITEM_BOTTLE_EMPTY;
            case BLOCK_NOTE_BLOCK_HAT:
                return Sound.BLOCK_NOTE_BLOCK_HAT;
            case BLOCK_NOTE_BLOCK_BELL:
                return Sound.BLOCK_NOTE_BLOCK_BELL;
            case BLOCK_WET_GRASS_BREAK:
                return Sound.BLOCK_WET_GRASS_BREAK;
            case BLOCK_NOTE_BLOCK_CHIME:
                return Sound.BLOCK_NOTE_BLOCK_CHIME;
            case ENTITY_ENDERMAN_TELEPORT:
                return Sound.ENTITY_ENDERMAN_TELEPORT;
            case BLOCK_CHEST_CLOSE:
                return Sound.BLOCK_CHEST_CLOSE;
            default:
                return null;
        }
    }

    public ItemStack getBed(Colors bedColor) {
        switch (bedColor){
            case RED:
                return new ItemStack(Material.RED_BED);
            case BLUE:
                return new ItemStack(Material.BLUE_BED);
            case CYAN:
                return new ItemStack(Material.CYAN_BED);
            case GRAY:
                return new ItemStack(Material.GRAY_BED);
            case LIME:
                return new ItemStack(Material.LIME_BED);
            case PINK:
                return new ItemStack(Material.PINK_BED);
            case BLACK:
                return new ItemStack(Material.BLACK_BED);
            case BROWN:
                return new ItemStack(Material.BROWN_BED);
            case GREEN:
                return new ItemStack(Material.GREEN_BED);
            case WHITE:
                return new ItemStack(Material.WHITE_BED);
            case ORANGE:
                return new ItemStack(Material.ORANGE_BED);
            case PURPLE:
                return new ItemStack(Material.PURPLE_BED);
            case YELLOW:
                return new ItemStack(Material.YELLOW_BED);
            case MAGENTA:
                return new ItemStack(Material.MAGENTA_BED);
            case LIGHT_BLUE:
                return new ItemStack(Material.LIGHT_BLUE_BED);
            case LIGHT_GRAY:
                return new ItemStack(Material.LIGHT_GRAY_BED);
            default:
                return null;
        }
    }

    public int getItemDurability(ItemStack itemStack) {
        return itemStack.getType().getMaxDurability() - ((Damageable) itemStack.getItemMeta()).getDamage();
    }

    public ItemStack getItemInHand(Player player, boolean isMainHand) {
        if (isMainHand){
            return player.getInventory().getItemInMainHand();
        }else {
            return player.getInventory().getItemInOffHand();
        }
    }


}

package com.ultikits.ultitools.utils;

import com.ultikits.enums.Colors;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.enums.LoginRegisterEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

import static com.ultikits.utils.MessagesUtils.warning;

public class GUIUtils {

    public static Map<String, InventoryManager> inventoryMap = new HashMap<>();

    private GUIUtils() {
    }

    public static void setupLoginRegisterLayout(Player player, @NotNull LoginRegisterEnum title) {
        File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        InventoryManager inventoryManager = new InventoryManager(player, 54, title.toString());
        inventoryMap.put(player.getName() + title.toString(), inventoryManager);

        ItemStack whiteGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.WHITE);
        ItemStackManager itemStackManager = new ItemStackManager(whiteGlass, UltiTools.languageUtils.getString("login_keyboard_button_label"));
        //数字键盘
        inventoryManager.setItem(21, itemStackManager.getItem(1));
        inventoryManager.setItem(22, itemStackManager.getItem(2));
        inventoryManager.setItem(23, itemStackManager.getItem(3));
        inventoryManager.setItem(30, itemStackManager.getItem(4));
        inventoryManager.setItem(31, itemStackManager.getItem(5));
        inventoryManager.setItem(32, itemStackManager.getItem(6));
        inventoryManager.setItem(39, itemStackManager.getItem(7));
        inventoryManager.setItem(40, itemStackManager.getItem(8));
        inventoryManager.setItem(41, itemStackManager.getItem(9));
        inventoryManager.setItem(49, itemStackManager.getItem(10));

        //其他功能键
        ItemStack blueGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.BLUE);
        ItemStack redGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED);
        ItemStack greenGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GREEN);
        ItemStack orangeGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.ORANGE);
        ItemStack grayGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GRAY);
        if (title == LoginRegisterEnum.LOGIN && UltiTools.getInstance().getConfig().getBoolean("enable_pro") && UltiTools.getInstance().getProChecker().getProStatus()) {
            ArrayList<String> lore = new ArrayList<>();
            ItemStackManager itemStackManager6;
            if (!config.getBoolean("registered")) {
                lore.add(warning(UltiTools.languageUtils.getString("emailregister_not_register_email_description")));
                itemStackManager6 = new ItemStackManager(blueGlass, lore, UltiTools.languageUtils.getString("button_forget_password"));
            } else {
                itemStackManager6 = new ItemStackManager(blueGlass, UltiTools.languageUtils.getString("button_forget_password"));
            }
            inventoryManager.setItem(45, itemStackManager6.getItem());
        }
        ItemStackManager itemStackManager2 = new ItemStackManager(redGlass, UltiTools.languageUtils.getString("button_clear"));
        inventoryManager.setItem(48, itemStackManager2.getItem());
        ItemStackManager itemStackManager3 = new ItemStackManager(greenGlass, UltiTools.languageUtils.getString("button_ok"));
        inventoryManager.setItem(50, itemStackManager3.getItem());
        ItemStackManager itemStackManager4 = new ItemStackManager(orangeGlass, UltiTools.languageUtils.getString("button_quit"));
        inventoryManager.setItem(53, itemStackManager4.getItem());
        ItemStackManager itemStackManager5 = new ItemStackManager(grayGlass, "");
        for (int i = 9; i < 54; i++) {
            if (inventoryManager.getInventory().getItem(i) == null) {
                inventoryManager.setItem(i, itemStackManager5.getItem());
            }
        }
    }

    public static void setupValidationCodeLayout(Player player) {
        try {
            InventoryManager inventoryManager = new InventoryManager(player, 54, LoginRegisterEnum.VALIDATION.toString());
            inventoryMap.put(player.getName() + LoginRegisterEnum.VALIDATION.toString(), inventoryManager);

            ItemStack whiteGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.WHITE);
            ItemStackManager itemStackManager = new ItemStackManager(whiteGlass, UltiTools.languageUtils.getString("login_keyboard_button_label"));
            //数字键盘
            inventoryManager.setItem(21, itemStackManager.getItem(1));
            inventoryManager.setItem(22, itemStackManager.getItem(2));
            inventoryManager.setItem(23, itemStackManager.getItem(3));
            inventoryManager.setItem(30, itemStackManager.getItem(4));
            inventoryManager.setItem(31, itemStackManager.getItem(5));
            inventoryManager.setItem(32, itemStackManager.getItem(6));
            inventoryManager.setItem(39, itemStackManager.getItem(7));
            inventoryManager.setItem(40, itemStackManager.getItem(8));
            inventoryManager.setItem(41, itemStackManager.getItem(9));

            //其他功能键
            ItemStack redGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED);
            ItemStack greenGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GREEN);
            ItemStack orangeGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.ORANGE);
            ItemStack grayGlass = UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GRAY);
            ItemStackManager itemStackManager2 = new ItemStackManager(redGlass, UltiTools.languageUtils.getString("button_clear"));
            inventoryManager.setItem(48, itemStackManager2.getItem());
            ItemStackManager itemStackManager3 = new ItemStackManager(greenGlass, UltiTools.languageUtils.getString("button_ok"));
            inventoryManager.setItem(50, itemStackManager3.getItem());
            ItemStackManager itemStackManager4 = new ItemStackManager(orangeGlass, UltiTools.languageUtils.getString("button_quit"));
            inventoryManager.setItem(53, itemStackManager4.getItem());
            ItemStackManager itemStackManager5 = new ItemStackManager(grayGlass, "");
            List<Integer> list = Arrays.asList(1, 2, 3, 5, 6, 7);
            for (int i = 0; i < 54; i++) {
                if (inventoryManager.getInventory().getItem(i) == null && !list.contains(i)) {
                    inventoryManager.setItem(i, itemStackManager5.getItem());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.ultikits.ultitools.listener;

import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.commands.LoginRegisterCommands;
import com.ultikits.ultitools.enums.LoginRegisterEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.GUIUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.ultikits.utils.MessagesUtils.warning;

@EventListener(function = "login")
public class ValidationPageListener implements Listener {

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        Inventory currentInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (event.getView().getTitle().equals(UltiTools.languageUtils.getString("login_validation_page_title"))) {
            if (clicked != null) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("login_keyboard_button_label"))) {
                    int slot = currentInventory.firstEmpty();
                    if (slot >= 1 && slot < 8 && slot != 4) {
                        currentInventory.setItem(slot, clicked);
                        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_HAT), 10, 1);
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_ok"))) {
                    if (!isCodeEnteredFully(currentInventory)){
                        player.sendMessage(warning(UltiTools.languageUtils.getString("emailregister_enter_validation_code")));
                    }
                    String password = "null";
                    try {
                        password = getTheCode(currentInventory);
                    } catch (Exception ignored) {
                    }
                    if (!validateTheCode(player, password)){
                        clearTheEnteringSlots(currentInventory);
                        player.sendMessage(warning(UltiTools.languageUtils.getString("emailregister_code_invalid")));
                    }else {
                        LoginGUIListener.playerIsValidating.put(player.getUniqueId(), false);
                        LoginGUIListener.isRegisteringNewPassword.put(player.getUniqueId(), true);
                        GUIUtils.setupLoginRegisterLayout(player, LoginRegisterEnum.REGISTER);
                        player.openInventory(GUIUtils.inventoryMap.get(player.getName() + LoginRegisterEnum.REGISTER.toString()).getInventory());
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_clear"))) {
                    clearTheEnteringSlots(currentInventory);
                } else if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getString("button_quit"))) {
                    LoginGUIListener.playerIsValidating.put(player.getUniqueId(), false);
                    player.openInventory(GUIUtils.inventoryMap.get(player.getName() + LoginRegisterEnum.LOGIN.toString()).getInventory());
                }
            }
        }
    }

    private void clearTheEnteringSlots(Inventory inventory) {
        for (int i = 1; i < 8; i++) {
            if (i == 4){
                continue;
            }
            inventory.setItem(i, null);
        }
    }

    private boolean validateTheCode(Player player, String code) {
        String correctCode = LoginRegisterCommands.playersValidateCode.get(player.getUniqueId());
        return code.equals(correctCode);
    }

    private String getTheCode(Inventory inventory) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < 8; i++) {
            if (i == 4){
                continue;
            }
            ItemStack itemStack = inventory.getItem(i);
            stringBuilder.append(itemStack.getAmount());
        }
        return stringBuilder.toString();
    }

    private boolean isCodeEnteredFully(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            if (inventory.getItem(i)==null){
                return false;
            }
        }
        return true;
    }
}

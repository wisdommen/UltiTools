package com.ultikits.ultitools.register;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.EmailUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PapiRegister extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "ultitools";
    }

    @Override
    public String getAuthor() {
        return "wisdomme";
    }

    @Override
    public String getVersion() {
        return UltiTools.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) {
            return "";
        }
        switch (params){
            case "new_email_num":
                return String.valueOf(EmailUtils.getUnReadEmailNum(player));
            default:
                return null;
        }
    }
}

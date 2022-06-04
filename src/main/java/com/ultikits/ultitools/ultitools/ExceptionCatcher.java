package com.ultikits.ultitools.ultitools;

import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.logging.Logger;

public class ExceptionCatcher {

    private static final Logger logger = UltiTools.getInstance().getLogger();

    public static void catchException(Exception exception) {
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            if (stackTraceElement.getClassName().contains("ultitools")) {
                logger.warning("===============================================================");
                logger.warning("            UltiTools Exception Catcher 异常捕获                ");
                logger.warning("                       这可能是一个BUG                           ");
                logger.warning("===============================================================");
                logger.warning(
                        "UltiTools版本号: "
                                + UltiTools.getInstance().getDescription().getVersion()
                                + " UltiCore版本号: "
                                + (Bukkit.getPluginManager().getPlugin("UltiCore") == null
                                ? "--" : Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("UltiCore")).getDescription().getVersion())
                                + " PlaceholderAPI版本号: "
                                + (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null
                                ? "--" : Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")).getDescription().getVersion())
                                + " Vault版本号: "
                                + (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null
                                ? "--" : Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Vault")).getDescription().getVersion())
                );
                logger.warning("===============================================================");
                logger.warning("Bukkit Version: " + Bukkit.getBukkitVersion() + "     Server Version: " + Bukkit.getVersion());
                logger.warning("===============================================================");
                logger.warning(exception.getMessage());
                logger.warning("===============================================================");
                logger.warning("                         StackTrace 调用栈                      ");
                logger.warning("ClassName           FileName           MethodName          Line");
                logger.warning("===============================================================");
                for (StackTraceElement ste : exception.getStackTrace())
                    logger.warning(ste.getClassName() + " " + ste.getFileName() + " " + ste.getMethodName() + " " + ste.getLineNumber());
                logger.warning("===============================================================");
                logger.warning("            请将以上信息反馈至官方QQ群或GitHub Issue               ");
                logger.warning("===============================================================");
                break;
            }
        }
    }
}

package com.ultikits.ultitools.utils;

import com.google.common.reflect.ClassPath;
import com.ultikits.ultitools.register.CommandRegister;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ScanUtils {
    public static void scanListeners(String listenerPackage, Plugin plugin) {
        try {
            ClassPath classPath = ClassPath.from(plugin.getClass().getClassLoader());
            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(listenerPackage)) {
                Class<?> c = Class.forName(classInfo.getName());
                if (Listener.class.isAssignableFrom(c) && plugin.getConfig().getBoolean(FunctionUtils.getAllListeners().get(c.getSimpleName()))) {
                    Bukkit.getServer().getPluginManager().registerEvents((Listener) c.getDeclaredConstructor().newInstance(), plugin);
                }
            }
        } catch (Exception e) {
            ExceptionUtils.catchException(e);
        }
    }

    public static void scanCommands(String commandPackage, Plugin plugin) {
        try {
            ClassPath classPath = ClassPath.from(plugin.getClass().getClassLoader());
            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(commandPackage)) {
                Class<?> c = Class.forName(classInfo.getName());
                if (CommandExecutor.class.isAssignableFrom(c) && plugin.getConfig().getBoolean(FunctionUtils.getAllCommands().get(c.getSimpleName()).get(0))) {
                    CommandRegister.registerCommand(
                            plugin,
                            (CommandExecutor) c.getDeclaredConstructor().newInstance(),
                            FunctionUtils.getAllCommands().get(c.getSimpleName()).get(1),
                            FunctionUtils.getAllCommands().get(c.getSimpleName()).get(2),
                            FunctionUtils.getAllCommands().get(c.getSimpleName()).get(3).split(",")
                    );
                }
            }
        } catch (Exception e) {
            ExceptionUtils.catchException(e);
        }
    }
}

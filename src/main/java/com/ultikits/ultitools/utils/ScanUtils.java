package com.ultikits.ultitools.utils;

import com.google.common.reflect.ClassPath;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.register.CommandRegister;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import static com.ultikits.ultitools.ultitools.UltiTools.languageUtils;

public class ScanUtils {
    public static void scanListeners(String listenerPackage, Plugin plugin) {
        try {
            ClassPath classPath = ClassPath.from(plugin.getClass().getClassLoader());
            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(listenerPackage)) {
                Class<?> c = Class.forName(classInfo.getName());
                if (!(c.isAnnotationPresent(EventListener.class) && c.isAssignableFrom(Listener.class))) continue;
                EventListener eventListener = c.getAnnotation(EventListener.class);
                if (
                        eventListener.function().equals("") ||
                                (
                                        FunctionUtils.getAllFunctions().contains(eventListener.function()) &&
                                                plugin.getConfig().getBoolean(FunctionUtils.getFunctionCode(
                                                        eventListener.function()
                                                ))
                                )
                ) Bukkit.getServer().getPluginManager().registerEvents((Listener) c.getDeclaredConstructor().newInstance(), plugin);
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
                if (!c.isAnnotationPresent(CmdExecutor.class)) continue;
                CmdExecutor cmdExecutor = c.getAnnotation(CmdExecutor.class);
                if (
                        cmdExecutor.function().equals("") ||
                                (
                                        FunctionUtils.getAllFunctions().contains(cmdExecutor.function()) &&
                                                plugin.getConfig().getBoolean(FunctionUtils.getFunctionCode(
                                                        cmdExecutor.function()
                                                ))
                                )
                ) {
                    CommandRegister.registerCommand(
                            plugin,
                            (CommandExecutor) c.getDeclaredConstructor().newInstance(),
                            cmdExecutor.permission(),
                            languageUtils.getString(cmdExecutor.description()),
                            cmdExecutor.alias().split(",")
                    );
                }
            }
        } catch (Exception e) {
            ExceptionUtils.catchException(e);
        }
    }
}

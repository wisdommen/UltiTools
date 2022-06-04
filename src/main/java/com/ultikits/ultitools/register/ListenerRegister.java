package com.ultikits.ultitools.register;

import com.ultikits.ultitools.ultitools.ExceptionCatcher;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.ClassUtils;
import org.bukkit.event.Listener;

import java.util.List;

public class ListenerRegister {
    public static void registerAll(String ListenerPackage) {
        List<String> classes = ClassUtils.scanClasses(UltiTools.getInstance(), ListenerPackage);
        for (String clazz : classes) {
            try {
                Class c = Class.forName(clazz);
                for (Class Interface : c.getInterfaces())
                    if (Interface.getName() == "org.bukkit.event.Listener")
                        UltiTools.getInstance().getServer().getPluginManager().registerEvents(
                                (Listener) c.newInstance(),
                                UltiTools.getInstance()
                        );
            } catch (Exception e) {
                ExceptionCatcher.catchException(e);
            }
        }
    }
}

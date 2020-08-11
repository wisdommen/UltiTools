package com.minecraft.ultikits.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class SerializationUtils {

    private SerializationUtils() {
    }

    public static @Nullable String serialize(@NotNull ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(outputStream);
            objectOutputStream.writeObject(item);
            objectOutputStream.flush();

            byte[] serializedObject = outputStream.toByteArray();

            return new String(Base64.getEncoder().encode(serializedObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static @Nullable ItemStack encodeToItem(String encodedString) {
        try {
            byte[] serializedObject = Base64.getDecoder().decode(encodedString);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);
            BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(inputStream);

            return (ItemStack) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

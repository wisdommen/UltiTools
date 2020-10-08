package com.minecraft.ultikits.multiversions;

import com.minecraft.ultikits.api.VersionWrapper;
import com.minecraft.ultikits.v1_10_R1.Wrapper1_10_R1;
import com.minecraft.ultikits.v1_11_R1.Wrapper1_11_R1;
import com.minecraft.ultikits.v1_12_R1.Wrapper1_12_R1;
import com.minecraft.ultikits.v1_13_R1.Wrapper1_13_R1;
import com.minecraft.ultikits.v1_13_R2.Wrapper1_13_R2;
import com.minecraft.ultikits.v1_14_4_R1.Wrapper1_14_4_R1;
import com.minecraft.ultikits.v1_14_R1.Wrapper1_14_R1;
import com.minecraft.ultikits.v1_15_R1.Wrapper1_15_R1;
import com.minecraft.ultikits.v1_16_R1.Wrapper1_16_R1;
import com.minecraft.ultikits.v1_16_R2.Wrapper1_16_R2;
import com.minecraft.ultikits.v1_16_R3.Wrapper1_16_R3;
import com.minecraft.ultikits.v1_8_R1.Wrapper1_8_R1;
import com.minecraft.ultikits.v1_8_R2.Wrapper1_8_R2;
import com.minecraft.ultikits.v1_8_R3.Wrapper1_8_R3;
import com.minecraft.ultikits.v1_9_R1.Wrapper1_9_R1;
import com.minecraft.ultikits.v1_9_R2.Wrapper1_9_R2;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

public class VersionAdaptor {

    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    private final List<Class<? extends VersionWrapper>> versions = Arrays.asList(
            Wrapper1_8_R1.class,
            Wrapper1_8_R2.class,
            Wrapper1_8_R3.class,
            Wrapper1_9_R1.class,
            Wrapper1_9_R2.class,
            Wrapper1_10_R1.class,
            Wrapper1_11_R1.class,
            Wrapper1_12_R1.class,
            Wrapper1_13_R1.class,
            Wrapper1_13_R2.class,
            Wrapper1_14_R1.class,
            Wrapper1_14_4_R1.class,
            Wrapper1_15_R1.class,
            Wrapper1_16_R1.class,
            Wrapper1_16_R2.class,
            Wrapper1_16_R3.class
    );

    public VersionWrapper match() {
        try {
            return versions.stream()
                    .filter(version -> version.getSimpleName().substring(7).equals(serverVersion))
                    .findFirst().orElseThrow(() -> new RuntimeException("Your server version isn't supported in UltiTools!"))
                    .newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
